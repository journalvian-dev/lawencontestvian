package com.java.backendtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.exception.DataNotFoundException;
import com.java.backendtest.service.OrderService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindOrders() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setOrderNo("O1");
        dto.setItemId(10L);
        dto.setQty(2L);

        when(orderService.findOrders(isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].orderNo").value("O1"));

        verify(orderService).findOrders(isNull(), any());
    }

    @Test
    void shouldFindOrderById() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setOrderNo("O99");

        when(orderService.findById("O99"))
                .thenReturn(dto);

        mockMvc.perform(get("/orders/O99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderNo").value("O99"));

        verify(orderService).findById("O99");
    }

    @Test
    void shouldCreateOrder() throws Exception {

        OrderDtoCreate req = new OrderDtoCreate();
        req.setItemId(1L);
        req.setQty(3L);

        OrderDto res = new OrderDto();
        res.setOrderNo("O2");

        when(orderService.saveOrder(any()))
                .thenReturn(res);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(orderService).saveOrder(any());
    }

    @Test
    void shouldReturn404WhenOrderNotFound() throws Exception {

        when(orderService.findById("O999"))
                .thenThrow(new DataNotFoundException("Order not found"));

        mockMvc.perform(get("/orders/O999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteOrder() throws Exception {

        doNothing().when(orderService).deleteOrder("O1");

        mockMvc.perform(delete("/orders/O1"))
                .andExpect(status().isOk());

        verify(orderService).deleteOrder("O1");
    }
}
