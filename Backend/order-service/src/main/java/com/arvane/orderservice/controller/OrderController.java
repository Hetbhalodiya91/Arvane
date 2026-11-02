package com.arvane.orderservice.controller;

import com.arvane.orderservice.dto.OrderRequest;
import com.arvane.orderservice.dto.OrderResponse;
import com.arvane.orderservice.entity.OrderStatus;
import com.arvane.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUser(@PathVariable String userEmail) {
        return ResponseEntity.ok(orderService.getOrdersByUserEmail(userEmail));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}