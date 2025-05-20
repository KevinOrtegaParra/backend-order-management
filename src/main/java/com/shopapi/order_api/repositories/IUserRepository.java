package com.shopapi.order_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapi.order_api.model.UserEntity;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long>{
    UserEntity findByEmail(String email);   
}
