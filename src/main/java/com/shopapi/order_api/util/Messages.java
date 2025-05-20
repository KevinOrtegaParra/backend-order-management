package com.shopapi.order_api.util;

public interface Messages {
        
    String NOT_FOUND = "Not found.";
    
    String GENERAL_ERROR = "General Error.";
    
    String USER_EXISTS = "User already exists.";
    
    String USER_NOT_EXIST = "User does not exist.";

    String PRODUCT_NOT_EXIST = "Product does not exist.";

    String ORDER_NOT_EXIST = "Order does not exist.";

    String CANCEL_PAID_ORDER = "You cannot cancel an already paid order.";
    
    String INVALID_OPERATION = "Invalid Operation.";

    String ORDER_NOT_PAYMENT = "Order not found for that PaymentIntent.";

}
