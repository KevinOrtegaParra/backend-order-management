package com.shopapi.order_api.dtos.users;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequestDTO implements Serializable{
    
    static final long serialVersionUID = 1L;

    @NotNull(message = "required name")
    String name;

    @NotNull(message = "required Email")
    @Email(message = "you have to have an email")
    String email;

    String password;

    @JsonIgnore
    Set<Long> roles;
}
