package com.shopapi.order_api.dtos.users;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class UserResponseDTO {
       
    Long id;
    
    String name;

    String email;
    
    @JsonIgnore
    String password;

    Set<String> roles;
    
    LocalDateTime createAt;

    LocalDateTime updatedAt;
    
}
