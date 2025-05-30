package com.shopapi.order_api.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class ErrorDto implements Serializable{
      
    static final long serialVersionUID = 1L;
    
    String error;

    String message;

    int status;

    LocalDateTime date;
    
    /**
     *  Obtiene nuevo error
     * @param error String Nombre error HTTP
     * @param message String Mensaje personalizado del error HTTP
     * @param statusint int Codigo error HTTP
     * @return ErrorDto
     */
    public static ErrorDto getErrorDto(String error, String message, int status) {
        return ErrorDto.builder()
                .error(error)
                .message(message)
                .status(status)
                .date(LocalDateTime.now())
                .build();
    }
}
