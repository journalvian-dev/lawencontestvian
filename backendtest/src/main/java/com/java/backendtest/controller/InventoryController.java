package com.java.backendtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.InventoryService;
import com.java.backendtest.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	ItemService itemService;
	
	
	@GetMapping
	public ResponseEntity<Page<InventoryDto>> findInventories(@RequestParam(required = false) Long itemId, Pageable pageable){
		return ResponseEntity.ok(inventoryService.findInventories(itemId, pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<InventoryDto>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>("Inventory found",inventoryService.findById(id)));
	}
	
	@PostMapping
	public ResponseEntity<InventoryDto> createInventory(@Valid @RequestBody InventoryDtoCreate dto){
			return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.createInventory(dto));
	}

	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<InventoryDto>> editInventory (@PathVariable Long id, @Valid @RequestBody InventoryDtoCreate inventDto) {
		return ResponseEntity.ok(new ApiResponse<>("Inventory edited successfully",inventoryService.updateInventory(id, inventDto)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteInventory (@PathVariable Long id) {
		inventoryService.deleteInventory(id);
		return ResponseEntity.ok(new ApiResponse<>("Inventory deleted successfully", null));
	}
}
