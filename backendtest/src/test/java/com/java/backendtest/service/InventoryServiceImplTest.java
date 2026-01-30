package com.java.backendtest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.entity.Inventory;
import com.java.backendtest.entity.Item;
import com.java.backendtest.enums.InventoryType;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    InventoryRepo inventoryRepo;

    @Mock
    ItemRepo itemRepo;

    @InjectMocks
    InventoryServiceImpl inventoryService;

    @Test
    void findAllTest() {
        Inventory inv = new Inventory();
        inv.setId(1L);

        Item item = new Item();
        item.setId(1L);
        inv.setItem(item);

        inv.setQty(5L);
        inv.setType(InventoryType.T);

        Page<Inventory> page = new PageImpl<>(List.of(inv));

        when(inventoryRepo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<?> result = inventoryService.findAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void saveInventoryTest() {
        Item item = new Item();
        item.setId(1L);

        Inventory saved = new Inventory();
        saved.setId(1L);
        saved.setItem(item);
        saved.setQty(10L);
        saved.setType(InventoryType.T);

        InventoryDtoCreate dto = new InventoryDtoCreate(1L, 10L, InventoryType.T);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepo.save(any(Inventory.class))).thenReturn(saved);

        var result = inventoryService.saveInventory(dto);

        assertNotNull(result);
        assertEquals(10L, result.getQty());
    }

    @Test
    void calculateStockTest() {
        when(inventoryRepo.calculateStock(1L)).thenReturn(35L);

        Long stock = inventoryService.calculateStock(1L);

        assertEquals(35L, stock);
    }
}
