package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;

public interface ItemService {
	Page<ItemDto> findAll(Pageable pageable);
	Page<ItemDto> findByNameSpecific(String name, Pageable pageable);
    Page<ItemDto> findByNameNonSpecific(String name, Pageable pageable);
	ItemDto findById(Long id);
	ItemDto saveItem (ItemDtoCreate itemDto);
	ItemDto updateItem (ItemDto itemDto);
	void updateItemForOtherController (ItemDto itemDto);
	void deleteItem(ItemDto itemDto);
	
}
