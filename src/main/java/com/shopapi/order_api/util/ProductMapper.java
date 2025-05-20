package com.shopapi.order_api.util;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.shopapi.order_api.dtos.products.ProductRequestDTO;
import com.shopapi.order_api.dtos.products.ProductResponseDTO;
import com.shopapi.order_api.model.Product;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toProduct(ProductRequestDTO productRequestDTO);

    ProductResponseDTO toProductResponseDTO(Product product);

    List<ProductResponseDTO> toProductResponseDTOList(List<Product> products);
    
}
