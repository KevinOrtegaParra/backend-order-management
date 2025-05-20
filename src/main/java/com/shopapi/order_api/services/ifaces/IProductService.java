package com.shopapi.order_api.services.ifaces;

import java.util.List;

import com.shopapi.order_api.dtos.products.ProductRequestDTO;
import com.shopapi.order_api.dtos.products.ProductRequestUpdateDTO;
import com.shopapi.order_api.dtos.products.ProductResponseDTO;
import com.shopapi.order_api.exceptions.RestException;

public interface IProductService {
    List<ProductResponseDTO> getProducts() throws RestException;

    ProductResponseDTO postProduct(ProductRequestDTO productRequestDTO) throws RestException;

    ProductResponseDTO update(ProductRequestUpdateDTO ProductRequestUpdateDTO, Long id) throws RestException;

    ProductResponseDTO findById(Long id) throws RestException;

    void deletById(Long id) throws RestException;
}
