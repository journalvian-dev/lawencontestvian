package com.java.backendtest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void findAllTest() throws Exception {
        when(orderService.findAll(PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/order/findAll"))
                .andExpect(status().isOk());
    }

    @Test
    void findByItemIdTest() throws Exception {
        when(orderService.findByItemId(1L, PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(
                get("/order/findByItemId")
                        .param("itemId", "1")
        ).andExpect(status().isOk());
    }

    @Test
    void findByIdTest() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setOrderNo("O1");

        when(orderService.findById("O1")).thenReturn(dto);

        mockMvc.perform(get("/order/String id/O1"))
                .andExpect(status().isOk());
    }

    @Test
    void saveOrderTest() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setOrderNo("O1");

        when(orderService.saveOrder(org.mockito.ArgumentMatchers.any()))
                .thenReturn(dto);

        mockMvc.perform(
                post("/order/saveOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "itemId": 1,
                              "qty": 2
                            }
                        """)
        ).andExpect(status().isOk());
    }

    @Test
    void editOrderTest() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setOrderNo("O1");

        when(orderService.updateOrder(org.mockito.ArgumentMatchers.any()))
                .thenReturn(dto);

        mockMvc.perform(
                post("/order/editOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "orderNo": "O1",
                              "itemId": 1,
                              "qty": 3
                            }
                        """)
        ).andExpect(status().isOk());
    }

    @Test
    void deleteOrderTest() throws Exception {
        mockMvc.perform(
                post("/order/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "orderNo": "O1"
                            }
                        """)
        ).andExpect(status().isOk());
    }
}
