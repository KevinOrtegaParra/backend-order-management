package com.shopapi.order_api.dtos.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopapi.order_api.model.OrderDetail;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequestUpdateDTO implements Serializable {
    
    static final long serialVersionUID = 1L;

    String name;

    String description;
    
    BigDecimal price;
    
    Integer stock;
    
    @JsonIgnore
    List<OrderDetail> details;

}
