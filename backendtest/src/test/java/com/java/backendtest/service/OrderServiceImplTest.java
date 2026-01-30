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

import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.entity.Order;
import com.java.backendtest.repo.ItemRepo;
import com.java.backendtest.repo.OrderRepo;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepo orderRepo;

    @Mock
    ItemRepo itemRepo;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void saveOrderTest() {
        // ====== setup item ======
        Item item = new Item();
        item.setId(1L);
        item.setPrice(5000L);

        // ====== mock last order number ======
        Page<String> lastOrderPage = new PageImpl<>(List.of("O1"));
        when(orderRepo.findLastOrderNo(PageRequest.of(0, 1)))
                .thenReturn(lastOrderPage);

        // ====== mock save result ======
        Order savedOrder = new Order();
        savedOrder.setOrderNo("O2");
        savedOrder.setItem(item);
        savedOrder.setQty(2L);
        savedOrder.setPrice(5000L);

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));
        when(orderRepo.save(any(Order.class))).thenReturn(savedOrder);

        // ====== execute ======
        OrderDtoCreate dtoCreate = new OrderDtoCreate(1L, 2L);
        var result = orderService.saveOrder(dtoCreate);

        // ====== assert ======
        assertNotNull(result);
        assertEquals("O2", result.getOrderNo());
        assertEquals(2L, result.getQty());
        assertEquals(5000L, result.getPrice());
    }

    @Test
    void findByIdTest() {
        Item item = new Item();
        item.setId(1L);
        item.setPrice(5000L);

        Order order = new Order();
        order.setOrderNo("O1");
        order.setItem(item);
        order.setQty(1L);
        order.setPrice(5000L);

        when(orderRepo.findById("O1")).thenReturn(Optional.of(order));

        var result = orderService.findById("O1");

        assertEquals("O1", result.getOrderNo());
        assertEquals(1L, result.getQty());
    }
}

