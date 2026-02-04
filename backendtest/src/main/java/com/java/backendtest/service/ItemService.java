package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;

public interface ItemService {
    Page<ItemDto> findItems(String name, Pageable pageable);
	ItemDto findById(Long id);
	ItemDto createItem (ItemDtoCreate itemDto);
	ItemDto updateItem (Long id, ItemDtoCreate itemDto);
	void deleteItem(Long id);
	
}
