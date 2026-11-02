package com.arvane.inventoryservice.dto;

import com.arvane.inventoryservice.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Boolean inStock;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InventoryResponse fromInventory(Inventory inventory) {
        return InventoryResponse.builder()
            .id(inventory.getId())
            .productId(inventory.getProductId())
            .quantity(inventory.getQuantity())
            .reservedQuantity(inventory.getReservedQuantity())
            .inStock(inventory.getQuantity() > 0)
            .message(inventory.getQuantity() > 0 ? "In stock" : "Out of stock")
            .createdAt(inventory.getCreatedAt())
            .updatedAt(inventory.getUpdatedAt())
            .build();
    }
}