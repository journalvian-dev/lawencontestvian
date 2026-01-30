package com.java.backendtest.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.java.backendtest.service.InventoryService;
import com.java.backendtest.service.ItemService;

import java.util.List;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InventoryService inventoryService;

    @MockBean
    ItemService itemService;

    @Test
    void findAllTest() throws Exception {
        when(inventoryService.findAll(PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/inventory/findAll"))
                .andExpect(status().isOk());
    }
}
