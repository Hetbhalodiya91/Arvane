package com.arvane.inventoryservice.controller;

import com.arvane.inventoryservice.dto.InventoryRequest;
import com.arvane.inventoryservice.dto.InventoryResponse;
import com.arvane.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        InventoryResponse response = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> createOrUpdateInventory(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.createOrUpdateInventory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{productId}/reserve/{quantity}")
    public ResponseEntity<Void> reserveInventory(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        inventoryService.reserveInventory(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/release/{quantity}")
    public ResponseEntity<Void> releaseReservedInventory(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        inventoryService.releaseReservedInventory(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{productId}/decrease/{quantity}")
    public ResponseEntity<Void> decreaseInventory(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        inventoryService.decreaseInventory(productId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}/availability/{quantity}")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
        boolean available = inventoryService.checkAvailability(productId, quantity);
        return ResponseEntity.ok(available);
    }
}