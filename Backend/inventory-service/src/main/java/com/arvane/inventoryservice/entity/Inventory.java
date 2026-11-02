package com.arvane.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isAvailable(Integer requestedQuantity) {
        return (quantity - reservedQuantity) >= requestedQuantity;
    }

    public boolean reserveStock(Integer quantityToReserve) {
        if (isAvailable(quantityToReserve)) {
            reservedQuantity += quantityToReserve;
            return true;
        }
        return false;
    }

    public void releaseReservedStock(Integer quantityToRelease) {
        reservedQuantity = Math.max(0, reservedQuantity - quantityToRelease);
    }

    public void decreaseStock(Integer quantityToDecrease) {
        if (quantity >= quantityToDecrease) {
            quantity -= quantityToDecrease;
            reservedQuantity = Math.max(0, reservedQuantity - quantityToDecrease);
        }
    }

    public void increaseStock(Integer quantityToAdd) {
        quantity += quantityToAdd;
    }
}