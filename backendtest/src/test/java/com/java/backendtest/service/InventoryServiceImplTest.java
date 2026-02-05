package com.java.backendtest.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.entity.Inventory;
import com.java.backendtest.entity.Item;
import com.java.backendtest.enums.InventoryType;
import com.java.backendtest.exception.InsufficientStockException;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepo inventoryRepo;

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void shouldCreateInventory_whenStockIsSufficient() {

        InventoryDtoCreate dto = new InventoryDtoCreate();
        dto.setItemId(1L);
        dto.setQty(5L);
        dto.setType(InventoryType.T);

        Item item = new Item();
        item.setId(1L);

        Inventory saved = new Inventory();
        saved.setId(10L);
        saved.setItem(item);
        saved.setQty(5L);
        saved.setType(InventoryType.T);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepo.save(any())).thenReturn(saved);

        InventoryDto result = inventoryService.createInventory(dto);

        assertNotNull(result);
        assertEquals(5L, result.getQty());

        verify(inventoryRepo).save(any());
    }


    @Test
    void shouldThrowException_whenWithdrawExceedsStock() {

        InventoryDtoCreate dto = new InventoryDtoCreate();
        dto.setItemId(1L);
        dto.setQty(10L);
        dto.setType(InventoryType.W);

        when(inventoryRepo.calculateStock(1L)).thenReturn(5L);

        assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.createInventory(dto)
        );

        verify(inventoryRepo, never()).save(any());
    }


    @Test
    void shouldDeleteInventory() {

        Inventory inventory = new Inventory();
        inventory.setId(1L);

        when(inventoryRepo.findById(1L))
                .thenReturn(Optional.of(inventory));

        inventoryService.deleteInventory(1L);

        verify(inventoryRepo).delete(inventory);
    }

    @Test
    void shouldCreateWithdrawInventory_whenStockIsEnough() {

        InventoryDtoCreate dto = new InventoryDtoCreate();
        dto.setItemId(1L);
        dto.setQty(5L);
        dto.setType(InventoryType.W);

        Item item = new Item();
        item.setId(1L);

        Inventory saved = new Inventory();
        saved.setId(10L);
        saved.setItem(item);
        saved.setQty(5L);
        saved.setType(InventoryType.W);

        when(inventoryRepo.calculateStock(1L))
                .thenReturn(10L);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        when(inventoryRepo.save(any()))
                .thenReturn(saved);

        InventoryDto result =
                inventoryService.createInventory(dto);

        assertEquals(5L, result.getQty());

        verify(inventoryRepo).save(any());
    }

    @Test
    void shouldThrowException_whenUpdateWithdrawExceedsStock() {

        Inventory existing = new Inventory();
        existing.setId(1L);
        existing.setQty(3L); 

        InventoryDtoCreate dto = new InventoryDtoCreate();
        dto.setItemId(1L);
        dto.setQty(10L);
        dto.setType(InventoryType.W);

        when(inventoryRepo.findById(1L))
                .thenReturn(Optional.of(existing));

        when(inventoryRepo.calculateStock(1L))
                .thenReturn(5L); 

        assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.updateInventory(1L, dto)
        );

        verify(inventoryRepo, never())
                .save(any());
    }

    @Test
    void shouldCreateTopup_withoutCheckingStock() {

        InventoryDtoCreate dto = new InventoryDtoCreate();
        dto.setItemId(1L);
        dto.setQty(100L);
        dto.setType(InventoryType.T);

        Item item = new Item();
        item.setId(1L);

        Inventory saved = new Inventory();
        saved.setId(99L);
        saved.setItem(item);
        saved.setQty(100L);
        saved.setType(InventoryType.T);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        when(inventoryRepo.save(any()))
                .thenReturn(saved);

        InventoryDto result =
                inventoryService.createInventory(dto);

        assertEquals(100L, result.getQty());

        verify(inventoryRepo, never())
                .calculateStock(any());
    }

}
