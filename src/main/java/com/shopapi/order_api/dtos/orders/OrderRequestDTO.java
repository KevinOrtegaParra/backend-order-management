package com.shopapi.order_api.dtos.orders;

import java.io.Serializable;
//import java.math.BigDecimal;
import java.util.List;

import com.shopapi.order_api.dtos.orderDetail.OrderDetailRequestDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.shopapi.order_api.model.OrderDetail;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderRequestDTO implements Serializable {

    static final long serialVersionUID = 1L;

    //BigDecimal total;

    //@JsonIgnore
    List<OrderDetailRequestDTO> details;
}
