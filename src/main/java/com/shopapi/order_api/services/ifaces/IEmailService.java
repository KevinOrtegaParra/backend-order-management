package com.shopapi.order_api.services.ifaces;

import com.shopapi.order_api.model.Order;

public interface IEmailService {
    boolean sendMail(String email, String name);
    boolean sendOrderConfirmationEmail(Order order);
}
