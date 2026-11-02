package com.arvane.orderservice.service;

import com.arvane.orderservice.dto.OrderItemResponse;
import com.arvane.orderservice.dto.OrderRequest;
import com.arvane.orderservice.dto.OrderResponse;
import com.arvane.orderservice.entity.Order;
import com.arvane.orderservice.entity.OrderItem;
import com.arvane.orderservice.entity.OrderStatus;
import com.arvane.orderservice.repository.OrderItemRepository;
import com.arvane.orderservice.repository.OrderRepository;
import com.arvane.orderservice.client.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;

    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .userEmail(request.getUserEmail())
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemRequest : request.getItems()) {
            Map<String, Object> productResponse = productClient.getProductById(itemRequest.getProductId());

            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.getProductId())
                    .productSku((String) productResponse.get("sku"))
                    .productName((String) productResponse.get("name"))
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(itemRequest.getUnitPrice())
                    .totalPrice(itemRequest.getUnitPrice().multiply(new BigDecimal(itemRequest.getQuantity())))
                    .order(order)
                    .build();

            order.getItems().add(item);
            orderItemRepository.save(item);
            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }

    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapToResponse(order);
    }

    public List<OrderResponse> getOrdersByUserEmail(String userEmail) {
        return orderRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userEmail(order.getUserEmail())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }

    private OrderItemResponse mapItemToResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productSku(item.getProductSku())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}