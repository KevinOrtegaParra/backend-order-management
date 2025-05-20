package com.shopapi.order_api.dtos.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.shopapi.order_api.dtos.orderDetail.OrderDetailResponseDTO;
import com.shopapi.order_api.dtos.users.UserResponseDTO;
//import com.shopapi.order_api.model.OrderDetail;
import com.shopapi.order_api.model.OrderStatus;

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
public class OrderResponseDTO {
 
    Long id; 

    BigDecimal total;

    OrderStatus status;

    String paymentIntentId;
    
    UserResponseDTO user;

    List<OrderDetailResponseDTO> details;

    LocalDateTime createAt;

    LocalDateTime updatedAt;
}

