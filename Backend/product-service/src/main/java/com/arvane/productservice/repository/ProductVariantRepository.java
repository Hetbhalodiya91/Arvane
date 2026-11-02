package com.arvane.productservice.repository;

import com.arvane.productservice.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);
    List<ProductVariant> findByProductId(Long productId);
    List<ProductVariant> findByProductIdAndIsActiveTrue(Long productId);
}