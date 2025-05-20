package com.shopapi.order_api.dtos.users;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequestUpdateDTO implements Serializable{
    
    static final long serialVersionUID = 1L;

    String name;

    String password;
}
