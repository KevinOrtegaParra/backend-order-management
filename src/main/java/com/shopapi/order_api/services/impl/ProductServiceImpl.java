package com.shopapi.order_api.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapi.order_api.dtos.products.ProductRequestDTO;
import com.shopapi.order_api.dtos.products.ProductRequestUpdateDTO;
import com.shopapi.order_api.dtos.products.ProductResponseDTO;
import com.shopapi.order_api.exceptions.ErrorDto;
import com.shopapi.order_api.exceptions.InternalServerErrorException;
import com.shopapi.order_api.exceptions.NotFoundException;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.model.Product;
import com.shopapi.order_api.repositories.IProductRepository;
import com.shopapi.order_api.services.ifaces.IProductService;
import com.shopapi.order_api.util.Messages;
import com.shopapi.order_api.util.ProductMapper;


@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<ProductResponseDTO> getProducts() throws RestException {
        try {
            return productMapper.toProductResponseDTOList(productRepository.findAll());
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
    @Transactional
    public ProductResponseDTO postProduct(ProductRequestDTO productRequestDTO) throws RestException {
        try {
            return productMapper
                    .toProductResponseDTO(productRepository.save(productMapper.toProduct(productRequestDTO)));
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
    @Transactional
    public ProductResponseDTO update(ProductRequestUpdateDTO productRequestUpdateDTO, Long id)
            throws RestException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorDto.builder()
                                .error(Messages.NOT_FOUND)
                                .message(Messages.PRODUCT_NOT_EXIST)
                                .status(HttpStatus.NOT_FOUND.value())
                                .date(LocalDateTime.now())
                                .build()));

        product.setName(productRequestUpdateDTO.getName() != null ? productRequestUpdateDTO.getName() : product.getName());
        product.setDescription(productRequestUpdateDTO.getDescription() != null ? productRequestUpdateDTO.getDescription()
                : product.getDescription());
        product.setPrice(productRequestUpdateDTO.getPrice() != null ? productRequestUpdateDTO.getPrice() : product.getPrice());
        product.setStock(productRequestUpdateDTO.getStock() != null ? productRequestUpdateDTO.getStock() : product.getStock());

        try {
            return productMapper.toProductResponseDTO(productRepository.save(product));
        } catch (Exception e) {
            throw new InternalServerErrorException(
                ErrorDto.builder()
                .error(Messages.NOT_FOUND)
                .message(Messages.PRODUCT_NOT_EXIST)
                .status(HttpStatus.NOT_FOUND.value())
                .date(LocalDateTime.now())
                .build()
            );
        }
    }

    @Override
    public ProductResponseDTO findById(Long id) throws RestException {
        return productMapper.toProductResponseDTO(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorDto.builder()
                                .error(Messages.NOT_FOUND)
                                .message(Messages.PRODUCT_NOT_EXIST)
                                .status(HttpStatus.NOT_FOUND.value())
                                .date(LocalDateTime.now())
                                .build())));
    }

    @Override
    @Transactional
    public void deletById(Long id) throws RestException {
        productRepository.deleteById(id);
    }

}
