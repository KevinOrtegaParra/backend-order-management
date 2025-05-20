package com.shopapi.order_api.services.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopapi.order_api.dtos.users.UserRequestDTO;
import com.shopapi.order_api.dtos.users.UserRequestUpdateDTO;
import com.shopapi.order_api.dtos.users.UserResponseDTO;
import com.shopapi.order_api.exceptions.BadRequestException;
import com.shopapi.order_api.exceptions.ErrorDto;
import com.shopapi.order_api.exceptions.InternalServerErrorException;
import com.shopapi.order_api.exceptions.NotFoundException;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.model.Role;
import com.shopapi.order_api.model.UserEntity;
import com.shopapi.order_api.repositories.IUserRepository;
import com.shopapi.order_api.services.ifaces.IEmailService;
import com.shopapi.order_api.services.ifaces.IUserService;
import com.shopapi.order_api.util.Messages;
import com.shopapi.order_api.util.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getUsers() throws RestException {
        log.info("consultUsers UserService");
        try {
            return userMapper.toUserResponseDTOList(userRepository.findAll());

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public UserResponseDTO register(UserRequestDTO userRequestDTO) throws RestException {
        UserEntity user = userRepository.findByEmail(userRequestDTO.getEmail());

        if (user != null) {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .message(Messages.USER_EXISTS)
                            .status(HttpStatus.BAD_REQUEST.value())
                            .date(LocalDateTime.now())
                            .build());
        }

        try {
            user = userMapper.toUser(userRequestDTO);
            user.setRoles(Collections.singleton(new Role(2L)));

            String passwordEncoded = passwordEncoder.encode(user.getPassword());
            user.setPassword(passwordEncoded);
            user = userRepository.save(user);

            if (user != null) {
                if (emailService.sendMail(user.getEmail(), user.getName())) {
                    log.info("Mensaje enviado");
                } else {
                    log.warn("Mensaje no enviado");
                }
            }

            return userMapper.toUserResponseDTO(user);

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }

    }

    @Override
    public UserResponseDTO update(UserRequestUpdateDTO user, Authentication authentication) throws RestException {

        UserEntity userDB = userRepository.findByEmail(authentication.getName());
        if (userDB == null) {
            throw new NotFoundException(
                    ErrorDto.builder()
                            .error(Messages.NOT_FOUND)
                            .message(Messages.USER_NOT_EXIST)
                            .status(404)
                            .date(LocalDateTime.now())
                            .build());
        }

        userDB.setName(user.getName() != null ? user.getName() : userDB.getName());
        if (user.getPassword() != null) {
            String passwordEncoded = passwordEncoder.encode(user.getPassword());
            userDB.setPassword(passwordEncoded);
        }

        try {
            return userMapper.toUserResponseDTO(userRepository.save(userDB));

        } catch (Exception e) {
            throw new InternalServerErrorException(
                    ErrorDto.builder()
                            .error(Messages.GENERAL_ERROR)
                            .message(e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .date(LocalDateTime.now())
                            .build());
        }
    }

    @Override
    public UserResponseDTO findById(Long id) throws RestException {
        log.info("queryUserById UserService");
        return userMapper.toUserResponseDTO(userRepository.findById(id).orElseThrow(() -> new NotFoundException(
                ErrorDto.builder()
                        .error(Messages.NOT_FOUND)
                        .message(Messages.USER_NOT_EXIST)
                        .status(HttpStatus.NOT_FOUND.value())
                        .date(LocalDateTime.now())
                        .build())));
    }

    @Override
    public void deletById(Long id) throws RestException {
        log.info("deleteUserById UserService");
        userRepository.deleteById(id);
    }

}
