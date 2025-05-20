package com.shopapi.order_api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapi.order_api.dtos.orders.OrderRequestDTO;
import com.shopapi.order_api.dtos.orders.OrderResponseDTO;
import com.shopapi.order_api.exceptions.BadRequestException;
import com.shopapi.order_api.exceptions.ErrorDto;
import com.shopapi.order_api.exceptions.InternalServerErrorException;
import com.shopapi.order_api.exceptions.NotFoundException;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.model.Order;
import com.shopapi.order_api.model.OrderDetail;
import com.shopapi.order_api.model.OrderStatus;
import com.shopapi.order_api.model.Product;
import com.shopapi.order_api.model.UserEntity;
import com.shopapi.order_api.repositories.IOrdeRepository;
import com.shopapi.order_api.repositories.IOrderDetailRepository;
import com.shopapi.order_api.repositories.IProductRepository;
import com.shopapi.order_api.repositories.IUserRepository;
import com.shopapi.order_api.services.ifaces.IEmailService;
import com.shopapi.order_api.services.ifaces.IOrderService;
import com.shopapi.order_api.services.ifaces.IStripeService;
import com.shopapi.order_api.util.Messages;
import com.shopapi.order_api.util.OrderMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IOrdeRepository ordeRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IOrderDetailRepository ordeDetailRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private IStripeService stripeService;

    @Override
    public List<OrderResponseDTO> getUserOrder(Authentication authentication) throws RestException {
        UserEntity user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new NotFoundException(
                    ErrorDto.builder()
                            .error(Messages.NOT_FOUND)
                            .message(Messages.USER_NOT_EXIST)
                            .status(HttpStatus.NOT_FOUND.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        try {
            return orderMapper.toOrderResponseDTOList(ordeRepository.findAllByUser(user));
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Transactional
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Authentication authentication)
            throws RestException {
        UserEntity user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new NotFoundException(
                    ErrorDto.builder()
                            .error(Messages.NOT_FOUND)
                            .message(Messages.USER_NOT_EXIST)
                            .status(HttpStatus.NOT_FOUND.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        Order order = orderMapper.toOrder(orderRequestDTO);
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        try {

            BigDecimal total = BigDecimal.ZERO;

            List<OrderDetail> details = orderMapper.tOrderDetailList(orderRequestDTO.getDetails());
            for (OrderDetail detail : details) {
                detail.setOrder(order);
                Product product = productRepository.findById(detail.getProduct().getId())
                        .orElseThrow(() -> new NotFoundException(
                                ErrorDto.builder()
                                        .error(Messages.NOT_FOUND)
                                        .message(Messages.PRODUCT_NOT_EXIST)
                                        .status(HttpStatus.NOT_FOUND.value())
                                        .date(LocalDateTime.now())
                                        .build()));

                if (product.getStock() < detail.getQuantity()) {
                    throw new BadRequestException("Insufficient stock for the product: " + product.getName());
                }

                product.setStock(product.getStock() - detail.getQuantity());
                product = productRepository.save(product);

                detail.setProduct(product);
                BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
                total = total.add(lineTotal);
            }

            order.setTotal(total);
            order.setDetails(details);
            order.setPaymentIntentId(
                    stripeService.createPaymentIntent(
                            total.multiply(BigDecimal.valueOf(100)).longValueExact(),
                            "usd").getId());
            order = ordeRepository.save(order);
            ordeDetailRepository.saveAll(details);

            return orderMapper.tOrderResponseDTO(order);

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public OrderResponseDTO cancelOrder(Long id, Authentication authentication) throws RestException {
        UserEntity user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            throw new NotFoundException(
                    ErrorDto.builder()
                            .error(Messages.NOT_FOUND)
                            .message(Messages.USER_NOT_EXIST)
                            .status(HttpStatus.NOT_FOUND.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        Order order = ordeRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.ORDER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build()));

        List<OrderDetail> details = order.getDetails();

        if (order.getStatus() == OrderStatus.PAID) {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .error(Messages.INVALID_OPERATION)
                            .message(Messages.CANCEL_PAID_ORDER)
                            .status(HttpStatus.BAD_REQUEST.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        if (order.getStatus() == OrderStatus.PENDING) {
            for (OrderDetail detail : details) {
                Product product = productRepository.findById(detail.getProduct().getId())
                        .orElseThrow(() -> new NotFoundException(
                                ErrorDto.builder()
                                        .error(Messages.NOT_FOUND)
                                        .message(Messages.PRODUCT_NOT_EXIST)
                                        .status(HttpStatus.NOT_FOUND.value())
                                        .date(LocalDateTime.now())
                                        .build()));
                product.setStock(product.getStock() + detail.getQuantity());
                productRepository.save(product);
            }
            order.setStatus(OrderStatus.CANCELED);
        }

        try {
            stripeService.cancelPaymentIntent(order.getPaymentIntentId()).getId();
            return orderMapper.tOrderResponseDTO(ordeRepository.save(order));
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public void markOrderAsPaid(String paymentIntentId) throws NotFoundException {
        Order order = ordeRepository.findByPaymentIntentId(paymentIntentId);

        if (order == null) {
            log.error("No se encontró una orden con PaymentIntent ID: {}", paymentIntentId);
            throw new NotFoundException(
                    ErrorDto.builder()
                            .error(Messages.NOT_FOUND)
                            .message(Messages.ORDER_NOT_PAYMENT)
                            .status(HttpStatus.NOT_FOUND.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        if (order.getStatus() == OrderStatus.PAID) {
            log.info("La orden ya estaba marcada como pagada: {}", order.getId());
            return;
        }

        order.setStatus(OrderStatus.PAID);
        ordeRepository.save(order);

        //Correo de confirmacion 
        if (emailService.sendOrderConfirmationEmail(order)) {
            log.info("Correo de confirmación enviado para la orden con ID: {}", order.getId());
        } else {
            log.warn("No se pudo enviar el correo para la orden con ID: {}", order.getId());
        }
    }

}
