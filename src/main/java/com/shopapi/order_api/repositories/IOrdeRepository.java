package com.shopapi.order_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapi.order_api.model.Order;
import com.shopapi.order_api.model.UserEntity;

@Repository
public interface IOrdeRepository extends JpaRepository<Order, Long>{
    
    List<Order> findAllByUser(UserEntity user); 

    Order findByPaymentIntentId(String paymentIntentId);

}
