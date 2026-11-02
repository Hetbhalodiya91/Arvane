package com.arvane.productservice.controller;

import com.arvane.productservice.dto.ProductVariantRequest;
import com.arvane.productservice.dto.ProductVariantResponse;
import com.arvane.productservice.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final ProductVariantService variantService;

    @PostMapping
    public ResponseEntity<ProductVariantResponse> addVariant(
            @PathVariable Long productId,
            @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(variantService.addVariant(productId, request));
    }

    @GetMapping
    public ResponseEntity<List<ProductVariantResponse>> getVariants(@PathVariable Long productId) {
        return ResponseEntity.ok(variantService.getVariantsByProduct(productId));
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponse> updateVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @RequestBody ProductVariantRequest request
    ) {
        return ResponseEntity.ok(variantService.updateVariant(variantId, request));
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<Void> deleteVariant(@PathVariable Long variantId) {
        variantService.deleteVariant(variantId);
        return ResponseEntity.noContent().build();
    }
}