package com.arvane.inventoryservice;

import com.arvane.inventoryservice.dto.InventoryRequest;
import com.arvane.inventoryservice.dto.InventoryResponse;
import com.arvane.inventoryservice.entity.Inventory;
import com.arvane.inventoryservice.repository.InventoryRepository;
import com.arvane.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void getInventoryByProductId_shouldReturnInventory() {
        Inventory inventory = Inventory.builder()
            .productId(1L)
            .quantity(10)
            .reservedQuantity(0)
            .build();

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        InventoryResponse response = inventoryService.getInventoryByProductId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals(10, response.getQuantity());
        assertEquals(0, response.getReservedQuantity());
        assertTrue(response.getInStock());
    }

    @Test
    void createOrUpdateInventory_shouldCreateNewInventory() {
        InventoryRequest request = InventoryRequest.builder()
            .productId(2L)
            .quantity(5)
            .build();

        when(inventoryRepository.findByProductId(2L)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryResponse response = inventoryService.createOrUpdateInventory(request);

        assertNotNull(response);
        assertEquals(2L, response.getProductId());
        assertEquals(5, response.getQuantity());
    }

    @Test
    void createOrUpdateInventory_shouldIncreaseQuantity() {
        Inventory existingInventory = Inventory.builder()
            .productId(3L)
            .quantity(10)
            .reservedQuantity(0)
            .build();

        InventoryRequest request = InventoryRequest.builder()
            .productId(3L)
            .quantity(5)
            .build();

        when(inventoryRepository.findByProductId(3L)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InventoryResponse response = inventoryService.createOrUpdateInventory(request);

        assertNotNull(response);
        assertEquals(3L, response.getProductId());
        assertEquals(15, response.getQuantity());
    }

    @Test
    void checkAvailability_shouldReturnTrue() {
        Inventory inventory = Inventory.builder()
            .productId(4L)
            .quantity(10)
            .reservedQuantity(2)
            .build();

        when(inventoryRepository.findByProductId(4L)).thenReturn(Optional.of(inventory));

        boolean available = inventoryService.checkAvailability(4L, 5);

        assertTrue(available);
    }

    @Test
    void checkAvailability_shouldReturnFalse() {
        Inventory inventory = Inventory.builder()
            .productId(5L)
            .quantity(10)
            .reservedQuantity(8)
            .build();

        when(inventoryRepository.findByProductId(5L)).thenReturn(Optional.of(inventory));

        boolean available = inventoryService.checkAvailability(5L, 5);

        assertFalse(available);
    }
}