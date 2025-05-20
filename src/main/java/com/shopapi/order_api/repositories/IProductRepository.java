package com.shopapi.order_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapi.order_api.model.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>{
    
}
