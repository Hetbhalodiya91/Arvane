package com.arvane.paymentservice.repository;

import com.arvane.paymentservice.entity.Payment;
import com.arvane.paymentservice.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByUserEmail(String userEmail);
    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
    List<Payment> findByStatusAndUserEmail(PaymentStatus status, String userEmail);
    Optional<Payment> findByTransactionId(String transactionId);
}