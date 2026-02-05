package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;

public interface InventoryService {
	Page<InventoryDto> findInventories(Long itemId, Pageable pageable);
	InventoryDto findById(Long id);
	InventoryDto createInventory(InventoryDtoCreate dto);
	InventoryDto updateInventory(Long id, InventoryDtoCreate dto);
	void deleteInventory(Long id);


}
