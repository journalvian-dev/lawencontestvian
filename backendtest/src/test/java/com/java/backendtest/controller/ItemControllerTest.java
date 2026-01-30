package com.java.backendtest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.service.ItemService;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void findAllTest() throws Exception {
        Page<ItemDto> page =
                new PageImpl<>(List.of(new ItemDto(1L, "Pen", 5L, null)));

        when(itemService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/item/findAll"))
                .andExpect(status().isOk());
    }

    @Test
    void findByIdTest() throws Exception {
        ItemDto dto = new ItemDto(1L, "Pen", 5L, null);
        when(itemService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/item/findById/1"))
                .andExpect(status().isOk());
    }

    @Test
    void saveItemTest() throws Exception {
        ItemDto dto = new ItemDto(1L, "Pen", 5L, null);
        when(itemService.saveItem(any())).thenReturn(dto);

        ItemDtoCreate req = new ItemDtoCreate("Pen", 5L);

        mockMvc.perform(post("/item/saveItem/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void saveItemValidationFailTest() throws Exception {
        ItemDtoCreate req = new ItemDtoCreate("", null);

        mockMvc.perform(post("/item/saveItem/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editItemTest() throws Exception {
        ItemDto dto = new ItemDto(1L, "Pen", 15L, null);
        when(itemService.updateItem(any())).thenReturn(dto);

        mockMvc.perform(post("/item/editItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItemTest() throws Exception {
        mockMvc.perform(post("/item/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new ItemDto(1L, "Pen", 5L, null))))
                .andExpect(status().isOk());
    }
}
