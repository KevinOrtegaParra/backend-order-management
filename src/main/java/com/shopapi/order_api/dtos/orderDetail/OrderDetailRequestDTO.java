package com.shopapi.order_api.dtos.orderDetail;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderDetailRequestDTO implements Serializable{
    static final long serialVersionUID = 1L;
 
    Integer quantity;

    Long product;
}
