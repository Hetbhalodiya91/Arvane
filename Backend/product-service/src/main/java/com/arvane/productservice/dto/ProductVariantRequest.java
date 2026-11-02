package com.arvane.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantRequest {
    @NotBlank
    @Size(max = 50)
    private String sku;

    @Size(max = 50)
    private String color;

    @Size(max = 20)
    private String size;

    @NotNull
    @Min(0)
    private Integer stockQuantity = 0;

    private BigDecimal priceOverride;

    private String imageUrl;

    private Boolean isActive = true;
}