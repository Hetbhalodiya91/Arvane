package com.arvane.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponse {
    private Long id;
    private String sku;
    private String color;
    private String size;
    private Integer stockQuantity;
    private BigDecimal priceOverride;
    private String imageUrl;
    private Boolean isActive;
}