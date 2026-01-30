package com.java.backendtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.InventoryService;
import com.java.backendtest.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	ItemService itemService;
	
	@GetMapping("/findAll")
	public ResponseEntity<Page<InventoryDto>> findAll(Pageable pageable){
		Page<InventoryDto> dtos = inventoryService.findAll(pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findByItem")
	public ResponseEntity<Page<InventoryDto>> findByItemId(Long itemId, Pageable pageable){
		Page<InventoryDto> dtos = inventoryService.findByItemId(itemId, pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<ApiResponse<InventoryDto>> findById(@PathVariable Long id) {
		InventoryDto dto = inventoryService.findById(id);
		return ResponseEntity.ok(new ApiResponse<>("Inventory found",dto));
	}
	
	@PostMapping("/saveInventory")
	public ResponseEntity<ApiResponse<InventoryDto>> saveInventory (@Valid @RequestBody InventoryDtoCreate inventDto) {
		InventoryDto dto = inventoryService.saveInventory(inventDto);
		Long stock = inventoryService.calculateStock(inventDto.getItemId());
		
		ItemDto itemDto = itemService.findById(inventDto.getItemId());
		itemDto.setStock(stock);
		itemService.updateItemForOtherController(itemDto);
		
		return ResponseEntity.ok(new ApiResponse<>("Inventory saved successfully",dto));
	}
	
	@PostMapping("/editInventory")
	public ResponseEntity<ApiResponse<InventoryDto>> editInventory (@Valid @RequestBody InventoryDto inventDto) {
		InventoryDto dto = inventoryService.updateInventory(inventDto);
		Long stock = inventoryService.calculateStock(inventDto.getItemId());
		
		ItemDto itemDto = itemService.findById(inventDto.getItemId());
		itemDto.setStock(stock);
		itemService.updateItemForOtherController(itemDto);
		
		return ResponseEntity.ok(new ApiResponse<>("Inventory edited successfully",dto));
	}
	
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<Void>> deleteInventory (@Valid @RequestBody InventoryDto itemDto) {
		inventoryService.deleteInventory(itemDto);
		return ResponseEntity.ok(new ApiResponse<>("Inventory deleted successfully", null));
	}
}
