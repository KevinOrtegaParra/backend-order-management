package com.shopapi.order_api.dtos.stripe;

public record PaymentResponse(String clientSecret, String paymentIntentId) {
    
}
