package com.shopapi.order_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopapi.order_api.dtos.orders.OrderRequestDTO;
import com.shopapi.order_api.dtos.orders.OrderResponseDTO;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.services.ifaces.IOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Order Controller", description = "Operations related to order management")
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @Operation(summary = "Show all Orders", description = "Endpoint to Show all orders")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(Authentication authentication) throws RestException {
        return ResponseEntity.ok(orderService.getUserOrder(authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "create a order", description = "Endpoint to create a order")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO order, Authentication authentication)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(order, authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Cancel order", description = "End point to cancel your order.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id, Authentication authentication)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.cancelOrder(id, authentication));
    }
}
