package com.arvane.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal basePrice;

    @DecimalMin("0.00")
    private BigDecimal salePrice;

    private String sku;

    private List<String> images;

    @NotNull
    private Long categoryId;

    private Boolean isActive = true;
    private Boolean isFeatured = false;
}