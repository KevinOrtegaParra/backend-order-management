package com.shopapi.order_api.exceptions;

public class RestException extends Exception{
    private static final long serialVersionUID = 1L;
    private ErrorDto errorDto;

    public RestException() {
        super();
    }

    public RestException(ErrorDto errorDto) {
        super(errorDto.getError());
        this.errorDto = errorDto;
    }

    public RestException(String msg) {
        super(msg);
    }

    public RestException(String msg, Exception ex) {
        super(msg, ex);
    }

    /**
     * @return the errorDto
     */
    public ErrorDto getErrorDto() {
        return errorDto;
    }

    /**
     * @param errorDto the errorDto to set
     */
    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }    
}
