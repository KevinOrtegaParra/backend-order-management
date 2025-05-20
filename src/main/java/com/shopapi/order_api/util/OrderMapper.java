package com.shopapi.order_api.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.shopapi.order_api.dtos.orderDetail.OrderDetailRequestDTO;
import com.shopapi.order_api.dtos.orderDetail.OrderDetailResponseDTO;
import com.shopapi.order_api.dtos.orders.OrderRequestDTO;
import com.shopapi.order_api.dtos.orders.OrderResponseDTO;
import com.shopapi.order_api.dtos.products.ProductResponseDTO;
import com.shopapi.order_api.model.Order;
import com.shopapi.order_api.model.OrderDetail;
import com.shopapi.order_api.model.Product;
import com.shopapi.order_api.model.Role;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order toOrder(OrderRequestDTO orderResponseDTO);

    OrderResponseDTO tOrderResponseDTO(Order order);

    List<OrderResponseDTO> toOrderResponseDTOList(List<Order> orders);

    List<OrderDetailResponseDTO> toOrderDetailResponseDTOList(List<OrderDetail> detail);

    OrderDetailResponseDTO toOrderDetailResponseDTO(OrderDetail detail);

    List<OrderDetail> tOrderDetailList(List<OrderDetailRequestDTO> detailRequestDTOs);

    ProductResponseDTO toProductResponseDTO(Product product);

    default Set<String> map(Set<Role> roles) {
        if (roles == null)
            return null;
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Product map(Long productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }

}
