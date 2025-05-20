package com.shopapi.order_api.dtos.products;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopapi.order_api.model.OrderDetail;

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
public class ProductResponseDTO {

    Long id;
    
    String name;
    
    String description;
    
    BigDecimal price;
    
    Integer stock;
    
    @JsonIgnore
    List<OrderDetail> details;

    LocalDateTime createAt;

    LocalDateTime updatedAt;
}
