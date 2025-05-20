package com.shopapi.order_api.services.ifaces;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.shopapi.order_api.dtos.users.UserRequestDTO;
import com.shopapi.order_api.dtos.users.UserRequestUpdateDTO;
import com.shopapi.order_api.dtos.users.UserResponseDTO;
import com.shopapi.order_api.exceptions.RestException;

public interface IUserService {
    List<UserResponseDTO> getUsers()throws RestException;
    
    UserResponseDTO register(UserRequestDTO userRequestDTO)throws RestException;
    
    UserResponseDTO update(UserRequestUpdateDTO user, Authentication authentication)throws RestException;
    
    UserResponseDTO findById(Long id)throws RestException;
    
    void deletById(Long id)throws RestException;
}
