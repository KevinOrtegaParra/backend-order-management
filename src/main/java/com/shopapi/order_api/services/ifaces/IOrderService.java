package com.shopapi.order_api.services.ifaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.shopapi.order_api.dtos.orders.OrderRequestDTO;
import com.shopapi.order_api.dtos.orders.OrderResponseDTO;
import com.shopapi.order_api.exceptions.RestException;

public interface IOrderService {
    List<OrderResponseDTO> getUserOrder(Authentication authentication) throws RestException;

    OrderResponseDTO createOrder(OrderRequestDTO order, Authentication authentication) throws RestException;

    OrderResponseDTO cancelOrder(Long id, Authentication authentication) throws RestException;

    void markOrderAsPaid(String paymentIntentId) throws RestException;
}