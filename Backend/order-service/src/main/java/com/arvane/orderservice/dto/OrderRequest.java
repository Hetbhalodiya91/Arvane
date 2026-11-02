package com.arvane.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotBlank
    private String userEmail;

    @NotBlank
    private String shippingAddress;

    @NotNull
    private List<OrderItemRequest> items;

    @NotBlank
    private String paymentMethod;
}