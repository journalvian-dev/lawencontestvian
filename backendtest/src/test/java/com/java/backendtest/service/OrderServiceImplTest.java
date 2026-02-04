package com.java.backendtest.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.entity.Order;
import com.java.backendtest.exception.InsufficientStockException;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;
import com.java.backendtest.repo.OrderRepo;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ItemRepo itemRepo;

    @Mock
    private InventoryRepo inventoryRepo;

    @InjectMocks
    private OrderServiceImpl service;

    private Item item;
    private Order order;

    @BeforeEach
    void setUp() {

        item = new Item();
        item.setId(1L);
        item.setPrice(100L);

        order = new Order();
        order.setOrderNo("O1");
        order.setItem(item);
        order.setQty(5L);
        order.setPrice(100L);
    }
    @Test
    void saveOrder_shouldCreateOrder_andWithdrawStock() {

        // ---------- GIVEN ----------
        OrderDtoCreate dto = new OrderDtoCreate();
        dto.setItemId(1L);
        dto.setQty(5L);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        when(inventoryRepo.calculateStock(1L))
                .thenReturn(10L);

        when(orderRepo.findLastOrderNo(any()))
                .thenReturn(Page.empty());

        when(orderRepo.save(any()))
                .thenReturn(order);

        OrderDto result = service.saveOrder(dto);

        assertNotNull(result);

        verify(orderRepo).save(any());
        verify(inventoryRepo).save(any()); // withdraw stock
    }

    @Test
    void saveOrder_shouldThrowException_whenStockNotEnough() {

        OrderDtoCreate dto = new OrderDtoCreate();
        dto.setItemId(1L);
        dto.setQty(20L);

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        when(inventoryRepo.calculateStock(1L))
                .thenReturn(5L);

        assertThrows(
                InsufficientStockException.class,
                () -> service.saveOrder(dto)
        );

        verify(orderRepo, never()).save(any());
    }
    @Test
    void updateOrder_shouldRecreateInventoryMovement() {

        OrderDtoCreate dto = new OrderDtoCreate();
        dto.setItemId(1L);
        dto.setQty(7L);

        when(orderRepo.findById("O1"))
                .thenReturn(Optional.of(order));

        when(itemRepo.findById(1L))
                .thenReturn(Optional.of(item));

        when(inventoryRepo.calculateStock(1L))
                .thenReturn(10L);

        when(orderRepo.save(any()))
                .thenReturn(order);

        service.updateOrder("O1", dto);

     
        verify(inventoryRepo, times(2)).save(any());
    }

}