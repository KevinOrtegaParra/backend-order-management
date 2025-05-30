package com.shopapi.order_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapi.order_api.dtos.stripe.PaymentResponse;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.services.ifaces.IOrderService;
import com.shopapi.order_api.services.ifaces.IStripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "stripe Controller", description = "Operations related to pago management")
@RequestMapping("/stripe")
@RestController
public class StripeController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private IOrderService orderService;

    private final IStripeService stripeService;

    @Autowired
    public StripeController(IStripeService stripeService) {
        this.stripeService = stripeService;
    }

    // Confirmar un PaymentIntent
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws RestException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (paymentIntent != null) {
                String paymentIntentId = paymentIntent.getId();
                orderService.markOrderAsPaid(paymentIntentId);
            }
        }

        return ResponseEntity.ok("Evento recibido");
    }

    // Cancelar un PaymentIntent
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelPaymentIntent(@PathVariable String id) {
        try {
            PaymentIntent paymentIntent = stripeService.cancelPaymentIntent(id);
            PaymentResponse response = new PaymentResponse(paymentIntent.getClientSecret(), paymentIntent.getId());
            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
