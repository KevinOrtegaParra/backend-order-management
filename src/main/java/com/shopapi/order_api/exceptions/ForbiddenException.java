/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.shopapi.order_api.exceptions;

/**
 *
 * @author kevin
 */
public class ForbiddenException extends RestException{
    
    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(ErrorDto errorDto) {
        super(errorDto);
    }

    public ForbiddenException(String msg) {
        super(msg);
    }
    
}
