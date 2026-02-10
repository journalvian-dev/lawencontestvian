package com.java.backendtest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.enums.InventoryType;
import com.java.backendtest.service.InventoryService;
import com.java.backendtest.service.ItemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backendtest.dto.InventoryDtoCreate;

import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;
    
    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindInventories() throws Exception {

        InventoryDto dto = new InventoryDto();
        dto.setId(1L);
        dto.setItemId(10L);
        dto.setQty(5L);
        dto.setType(InventoryType.T);

        when(inventoryService.findInventories(eq(null), any()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/inventories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1));

        verify(inventoryService).findInventories(eq(null), any());
    }

    @Test
    void shouldFindInventoryById() throws Exception {

        InventoryDto dto = new InventoryDto();
        dto.setId(1L);

        when(inventoryService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/inventories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void shouldCreateInventory() throws Exception {

        InventoryDtoCreate req = new InventoryDtoCreate();
        req.setItemId(1L);
        req.setQty(10L);
        req.setType(InventoryType.T);

        InventoryDto res = new InventoryDto();
        res.setId(99L);

        when(inventoryService.createInventory(any()))
                .thenReturn(res);

        mockMvc.perform(post("/inventories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(inventoryService).createInventory(any());
    }

    @Test
    void shouldUpdateInventory() throws Exception {

        InventoryDtoCreate req = new InventoryDtoCreate();
        req.setItemId(1L);
        req.setQty(20L);
        req.setType(InventoryType.T);

        InventoryDto res = new InventoryDto();
        res.setId(1L);

        when(inventoryService.updateInventory(eq(1L), any()))
                .thenReturn(res);

        mockMvc.perform(put("/inventories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(inventoryService).updateInventory(eq(1L), any());
    }

    @Test
    void shouldDeleteInventory() throws Exception {

        doNothing().when(inventoryService).deleteInventory(1L);

        mockMvc.perform(delete("/inventories/1"))
                .andExpect(status().isOk());

        verify(inventoryService).deleteInventory(1L);
    }
    
    
}
