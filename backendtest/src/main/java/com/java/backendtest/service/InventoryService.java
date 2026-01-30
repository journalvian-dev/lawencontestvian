package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;

public interface InventoryService {
	Page<InventoryDto> findAll(Pageable pageable);
	Page<InventoryDto> findByItemId(Long itemId, Pageable pageable);
	InventoryDto findById(Long id);
	InventoryDto saveInventory(InventoryDtoCreate inventDto);
	InventoryDto updateInventory(InventoryDto inventDto);
	void deleteInventory (InventoryDto itemDto);
	Long calculateStock(Long itemId);
}
