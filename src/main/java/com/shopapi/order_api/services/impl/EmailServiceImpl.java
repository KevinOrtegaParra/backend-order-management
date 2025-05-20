package com.shopapi.order_api.services.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.shopapi.order_api.model.Order;
import com.shopapi.order_api.model.OrderDetail;
import com.shopapi.order_api.services.ifaces.IEmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private SpringTemplateEngine engine;

    @Override
    public boolean sendMail(String email, String name) {
        boolean sent = false;
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", email);

        String htmlContent = engine.process("registration.html", context);
        sendHtmlEmail(email, "Bienvenido a Order", htmlContent);
        sent = true;

        return sent;
    }

    @Override
    public boolean sendOrderConfirmationEmail(Order order) {
        boolean sent = false;
        Context context = new Context();
        context.setVariable("name", order.getUser().getName());
        context.setVariable("totalAmount", order.getTotal());

        List<Map<String, Object>> productos = new ArrayList<>();
        for (OrderDetail detail : order.getDetails()) {
            Map<String, Object> producto = new HashMap<>();
            producto.put("productName", detail.getProduct().getName());
            producto.put("quantity", detail.getQuantity());
            producto.put("price", detail.getProduct().getPrice());
            productos.add(producto);
        }
        context.setVariable("products", productos);

        String htmlContent = engine.process("order.html", context);
        sendHtmlEmail(order.getUser().getEmail(), "Confirmación de pedido", htmlContent);
        sent = true;

        return sent;
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("kevin.ortegaap@est.iudigital.edu.co");
            helper.setTo(to);
            helper.setText(htmlContent, true);
            helper.setSubject(subject);
            sender.send(message);
            log.info("Mensaje enviado exitosamente!");

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }

}
