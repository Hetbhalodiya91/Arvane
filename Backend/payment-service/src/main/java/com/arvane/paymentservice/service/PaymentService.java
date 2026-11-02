package com.arvane.paymentservice.service;

import com.arvane.paymentservice.dto.PaymentRequest;
import com.arvane.paymentservice.dto.PaymentResponse;
import com.arvane.paymentservice.entity.Payment;
import com.arvane.paymentservice.entity.PaymentStatus;
import com.arvane.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.params.ChargeCreateParams;
import com.stripe.params.RefundCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${stripe.api-key}")
    private String stripeApiKey;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = stripeApiKey;
    }

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            // Create Stripe charge
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount(request.getAmount().multiply(BigDecimal.valueOf(100)).longValue()) // Convert to cents
                    .setCurrency(request.getCurrency().toLowerCase())
                    .setSource(request.getToken())
                    .setDescription("Payment for Order ID: " + request.getOrderId())
                    .build();

            Charge charge = Charge.create(params);

            // Create payment record
            Payment payment = Payment.builder()
                    .paymentId(UUID.randomUUID().toString())
                    .orderId(request.getOrderId())
                    .userEmail(request.getUserEmail())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(PaymentStatus.COMPLETED)
                    .paymentMethod(request.getPaymentMethod())
                    .transactionId(charge.getId())
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .id(savedPayment.getId())
                    .paymentId(savedPayment.getPaymentId())
                    .orderId(savedPayment.getOrderId())
                    .userEmail(savedPayment.getUserEmail())
                    .amount(savedPayment.getAmount())
                    .currency(savedPayment.getCurrency())
                    .status(savedPayment.getStatus())
                    .paymentMethod(savedPayment.getPaymentMethod())
                    .transactionId(savedPayment.getTransactionId())
                    .createdAt(savedPayment.getCreatedAt())
                    .updatedAt(savedPayment.getUpdatedAt())
                    .success(true)
                    .message("Payment processed successfully")
                    .build();

        } catch (StripeException e) {
            log.error("Stripe payment failed: {}", e.getMessage());

            // Create failed payment record
            Payment payment = Payment.builder()
                    .paymentId(UUID.randomUUID().toString())
                    .orderId(request.getOrderId())
                    .userEmail(request.getUserEmail())
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status(PaymentStatus.FAILED)
                    .paymentMethod(request.getPaymentMethod())
                    .build();

            Payment savedPayment = paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .id(savedPayment.getId())
                    .paymentId(savedPayment.getPaymentId())
                    .orderId(savedPayment.getOrderId())
                    .userEmail(savedPayment.getUserEmail())
                    .amount(savedPayment.getAmount())
                    .currency(savedPayment.getCurrency())
                    .status(savedPayment.getStatus())
                    .paymentMethod(savedPayment.getPaymentMethod())
                    .createdAt(savedPayment.getCreatedAt())
                    .updatedAt(savedPayment.getUpdatedAt())
                    .success(false)
                    .message("Payment failed: " + e.getMessage())
                    .build();
        }
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    public PaymentResponse getPaymentByPaymentId(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    public PaymentResponse refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Cannot refund a non-completed payment");
        }

        try {
            RefundCreateParams params = RefundCreateParams.builder()
                    .setCharge(payment.getTransactionId())
                    .build();

            Refund refund = Refund.create(params);

            payment.setStatus(PaymentStatus.REFUNDED);
            Payment savedPayment = paymentRepository.save(payment);

            return PaymentResponse.builder()
                    .id(savedPayment.getId())
                    .paymentId(savedPayment.getPaymentId())
                    .orderId(savedPayment.getOrderId())
                    .userEmail(savedPayment.getUserEmail())
                    .amount(savedPayment.getAmount())
                    .currency(savedPayment.getCurrency())
                    .status(savedPayment.getStatus())
                    .paymentMethod(savedPayment.getPaymentMethod())
                    .transactionId(refund.getId())
                    .createdAt(savedPayment.getCreatedAt())
                    .updatedAt(savedPayment.getUpdatedAt())
                    .success(true)
                    .message("Payment refunded successfully")
                    .build();

        } catch (StripeException e) {
            log.error("Stripe refund failed: {}", e.getMessage());
            throw new RuntimeException("Refund failed: " + e.getMessage());
        }
    }

    public java.util.List<PaymentResponse> getPaymentsByUserEmail(String userEmail) {
        return paymentRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    public java.util.List<PaymentResponse> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getStatus() == status)
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .userEmail(payment.getUserEmail())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .success(payment.getStatus() == PaymentStatus.COMPLETED || payment.getStatus() == PaymentStatus.REFUNDED)
                .message(payment.getStatus() == PaymentStatus.COMPLETED ? "Payment completed" :
                        payment.getStatus() == PaymentStatus.REFUNDED ? "Payment refunded" :
                        payment.getStatus() == PaymentStatus.FAILED ? "Payment failed" : "Payment pending")
                .build();
    }
}