package com.java.backendtest.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.repo.ItemRepo;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepo itemRepo;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void findAllTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(5L);

        Page<Item> page = new PageImpl<>(List.of(item));
        when(itemRepo.findAll(any(Pageable.class))).thenReturn(page);

        Page<ItemDto> result = itemService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByNameSpecificTest() {
        Item item = new Item();
        item.setName("Pen");

        Page<Item> page = new PageImpl<>(List.of(item));
        when(itemRepo.findByNameSpecific(eq("Pen"), any(Pageable.class)))
                .thenReturn(page);

        Page<ItemDto> result =
                itemService.findByNameSpecific("Pen", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByNameNonSpecificTest() {
        Item item = new Item();
        item.setName("Pen");

        Page<Item> page = new PageImpl<>(List.of(item));
        when(itemRepo.findByNameNonSpecific(eq("Pe"), any(Pageable.class)))
                .thenReturn(page);

        Page<ItemDto> result =
                itemService.findByNameNonSpecific("Pe", PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByIdTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(5L);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ItemDto result = itemService.findById(1L);

        assertEquals("Pen", result.getName());
    }

    @Test
    void saveItemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Book");
        item.setPrice(10L);

        when(itemRepo.save(any(Item.class))).thenReturn(item);

        ItemDtoCreate req = new ItemDtoCreate("Book", 10L);
        ItemDto result = itemService.saveItem(req);

        assertNotNull(result.getId());
    }

    @Test
    void updateItemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(5L);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepo.save(any(Item.class))).thenReturn(item);

        ItemDto req = new ItemDto(1L, "Item Updated", 15L, null);
        ItemDto result = itemService.updateItem(req);

        assertEquals("Item Updated", result.getName());
    }

    @Test
    void deleteItemTest() {
        Item item = new Item();
        item.setId(1L);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

        ItemDto req = new ItemDto(1L, "Pen", 15L, null);
        itemService.deleteItem(req);

        verify(itemRepo).delete(item);
    }
}

