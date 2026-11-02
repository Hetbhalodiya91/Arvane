package com.arvane.orderservice.repository;

import com.arvane.orderservice.entity.Order;
import com.arvane.orderservice.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findByUserEmail(String userEmail);
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    List<Order> findByUserEmailAndStatus(String userEmail, OrderStatus status);
}