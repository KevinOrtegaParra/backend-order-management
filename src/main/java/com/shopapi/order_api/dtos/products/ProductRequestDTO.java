package com.shopapi.order_api.dtos.products;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopapi.order_api.model.OrderDetail;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductRequestDTO implements Serializable {
    
    static final long serialVersionUID = 1L;

    @NotNull(message = "required name")
    String name;

    String description;
    
    @NotNull(message = "required price")
    BigDecimal price;
    
    Integer stock;
    
    @JsonIgnore
    List<OrderDetail> details;

}
