package com.java.backendtest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.exception.DataNotFoundException;
import com.java.backendtest.repo.ItemRepo;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void shouldCreateItem() {

        ItemDtoCreate dto = new ItemDtoCreate();
        dto.setName("Laptop");
        dto.setPrice(1000L);

        Item saved = new Item();
        saved.setId(1L);
        saved.setName("Laptop");
        saved.setPrice(1000L);

        when(itemRepo.save(any())).thenReturn(saved);

        ItemDto result = itemService.createItem(dto);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());

        verify(itemRepo).save(any());
    }


    @Test
    void shouldUpdateItem() {

        Item existing = new Item();
        existing.setId(1L);

        ItemDtoCreate dto = new ItemDtoCreate();
        dto.setName("Updated");
        dto.setPrice(2000L);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(existing));

        when(itemRepo.save(any()))
                .thenReturn(existing);

        itemService.updateItem(1L, dto);

        verify(itemRepo).save(existing);
    }


    @Test
    void shouldThrowException_whenItemNotFound() {

        when(itemRepo.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                DataNotFoundException.class,
                () -> itemService.updateItem(1L, new ItemDtoCreate())
        );
    }


    @Test
    void shouldDeleteItem() {

        Item item = new Item();
        item.setId(1L);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        itemService.deleteItem(1L);

        verify(itemRepo).delete(item);
    }
}
