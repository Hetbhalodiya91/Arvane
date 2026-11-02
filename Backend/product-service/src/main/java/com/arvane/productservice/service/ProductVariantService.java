package com.arvane.productservice.service;

import com.arvane.productservice.dto.ProductVariantRequest;
import com.arvane.productservice.dto.ProductVariantResponse;
import com.arvane.productservice.entity.Product;
import com.arvane.productservice.entity.ProductVariant;
import com.arvane.productservice.repository.ProductRepository;
import com.arvane.productservice.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    public ProductVariantResponse addVariant(Long productId, ProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (variantRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("Variant SKU already exists");
        }

        ProductVariant variant = mapToEntity(request, product);
        ProductVariant saved = variantRepository.save(variant);
        return mapToResponse(saved);
    }

    public List<ProductVariantResponse> getVariantsByProduct(Long productId) {
        return variantRepository.findByProductIdAndIsActiveTrue(productId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ProductVariantResponse updateVariant(Long variantId, ProductVariantRequest request) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        variant.setSku(request.getSku());
        variant.setColor(request.getColor());
        variant.setSize(request.getSize());
        variant.setStockQuantity(request.getStockQuantity());
        variant.setPriceOverride(request.getPriceOverride());
        variant.setImageUrl(request.getImageUrl());
        variant.setIsActive(request.getIsActive());

        ProductVariant saved = variantRepository.save(variant);
        return mapToResponse(saved);
    }

    public void deleteVariant(Long variantId) {
        if (!variantRepository.existsById(variantId)) {
            throw new RuntimeException("Variant not found");
        }
        variantRepository.deleteById(variantId);
    }

    private ProductVariant mapToEntity(ProductVariantRequest request, Product product) {
        return ProductVariant.builder()
                .sku(request.getSku())
                .color(request.getColor())
                .size(request.getSize())
                .stockQuantity(request.getStockQuantity())
                .priceOverride(request.getPriceOverride())
                .imageUrl(request.getImageUrl())
                .isActive(request.getIsActive())
                .product(product)
                .build();
    }

    private ProductVariantResponse mapToResponse(ProductVariant variant) {
        return ProductVariantResponse.builder()
                .id(variant.getId())
                .sku(variant.getSku())
                .color(variant.getColor())
                .size(variant.getSize())
                .stockQuantity(variant.getStockQuantity())
                .priceOverride(variant.getPriceOverride())
                .imageUrl(variant.getImageUrl())
                .isActive(variant.getIsActive())
                .build();
    }
}