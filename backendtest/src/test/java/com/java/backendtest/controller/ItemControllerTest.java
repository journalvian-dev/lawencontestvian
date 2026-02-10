package com.java.backendtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.service.ItemService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindItems() throws Exception {

        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Laptop");

        when(itemService.findItems(isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("Laptop"));

        verify(itemService).findItems(isNull(), any());
    }
    
    @Test
    void shouldFindItemById() throws Exception {

        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setName("Laptop");
        dto.setPrice(1000L);

        when(itemService.findById(1L))
                .thenReturn(dto);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Laptop"));

        verify(itemService).findById(1L);
    }


    @Test
    void shouldCreateItem() throws Exception {

        ItemDtoCreate req = new ItemDtoCreate();
        req.setName("Mouse");
        req.setPrice(50000L);

        ItemDto res = new ItemDto();
        res.setId(10L);

        when(itemService.createItem(any()))
                .thenReturn(res);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(itemService).createItem(any());
    }

    @Test
    void shouldUpdateItem() throws Exception {

        ItemDtoCreate req = new ItemDtoCreate();
        req.setName("Keyboard");
        req.setPrice(30L);

        when(itemService.updateItem(eq(1L), any()))
                .thenReturn(new ItemDto());

        mockMvc.perform(put("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(itemService).updateItem(eq(1L), any());
    }

    @Test
    void shouldDeleteItem() throws Exception {

        doNothing().when(itemService).deleteItem(1L);

        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());

        verify(itemService).deleteItem(1L);
    }
}

