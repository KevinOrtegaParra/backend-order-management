package com.shopapi.order_api.dtos.orderDetail;


import com.shopapi.order_api.dtos.products.ProductResponseDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponseDTO {
       
    Long id;
    
    Integer quantity;

    ProductResponseDTO product;
}
