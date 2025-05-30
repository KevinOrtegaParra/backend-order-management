package com.shopapi.order_api.services.ifaces;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface IStripeService {
    PaymentIntent createPaymentIntent(Long amount, String currency, Long productId, Long userId) throws StripeException;

    PaymentIntent confirmPaymentIntent(String id) throws StripeException;

    PaymentIntent cancelPaymentIntent(String id) throws StripeException;
}
