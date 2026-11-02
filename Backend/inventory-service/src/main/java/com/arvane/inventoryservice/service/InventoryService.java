package com.arvane.inventoryservice.service;

import com.arvane.inventoryservice.dto.InventoryRequest;
import com.arvane.inventoryservice.dto.InventoryResponse;
import com.arvane.inventoryservice.entity.Inventory;
import com.arvane.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        return InventoryResponse.fromInventory(inventory);
    }

    public InventoryResponse createOrUpdateInventory(InventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.getProductId())
                .map(existingInventory -> {
                    existingInventory.increaseStock(request.getQuantity());
                    return existingInventory;
                })
                .orElseGet(() -> Inventory.builder()
                        .productId(request.getProductId())
                        .quantity(request.getQuantity())
                        .reservedQuantity(0)
                        .build());

        Inventory saved = inventoryRepository.save(inventory);
        return InventoryResponse.fromInventory(saved);
    }

    public void decreaseInventory(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.isAvailable(quantity)) {
            inventory.decreaseStock(quantity);
            inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }
    }

    public void reserveInventory(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        boolean reserved = inventory.reserveStock(quantity);
        if (reserved) {
            inventoryRepository.save(inventory);
        } else {
            throw new RuntimeException("Insufficient available stock to reserve for product: " + productId);
        }
    }

    public void releaseReservedInventory(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        inventory.releaseReservedStock(quantity);
        inventoryRepository.save(inventory);
    }

    public boolean checkAvailability(Long productId, Integer quantity) {
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> inventory.isAvailable(quantity))
                .orElse(false);
    }
}