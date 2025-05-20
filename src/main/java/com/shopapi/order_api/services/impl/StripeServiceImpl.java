package com.shopapi.order_api.services.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.shopapi.order_api.services.ifaces.IStripeService;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripeServiceImpl implements IStripeService {

    public StripeServiceImpl(@Value("${stripe.secret.key}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency) throws StripeException {
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount) // El monto debe estar en centavos. Ejemplo: $10.00 = 1000
                    .setCurrency(currency)
                    .addPaymentMethodType("card")
                    .build();

            return PaymentIntent.create(params);
        }catch (CardException e) {
            throw e;
        }catch (Exception e) {
            throw e;
        }
    }

    @Override
    public PaymentIntent confirmPaymentIntent(String id) throws StripeException {
        return PaymentIntent.retrieve(id).confirm();
    }

    @Override
    public PaymentIntent cancelPaymentIntent(String id) throws StripeException {
        return PaymentIntent.retrieve(id).cancel();
    }
}
