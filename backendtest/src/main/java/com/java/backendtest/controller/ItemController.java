package com.java.backendtest.controller;

import java.time.Instant;

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

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/items")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping
	public ResponseEntity<ApiResponse<Page<ItemDto>>> findItems (@RequestParam(required = false) String name, Pageable pageable){
		Page<ItemDto> data = itemService.findItems(name, pageable);
		return ResponseEntity.ok(new ApiResponse<>(
				"Items fetched successfully",
				data,
				200,
				Instant.now()));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ItemDto>> findById(@PathVariable Long id) {
		ItemDto data = itemService.findById(id);
		return ResponseEntity.ok(new ApiResponse<>(
				"Item found",
				data,
				200,
				Instant.now()));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<ItemDto>>  saveItem (@Valid @RequestBody ItemDtoCreate itemDto) {
		ItemDto data =  itemService.createItem(itemDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
				"Item saved successfully",
				data,
				201,
				Instant.now()));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ItemDto>> editItem (@PathVariable Long id,  @Valid @RequestBody ItemDtoCreate itemDto) {
		ItemDto data = itemService.updateItem(id, itemDto);
		return ResponseEntity.ok(new ApiResponse<>(
				"Item edited successfully",
				data,
				200,
				Instant.now()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
		return ResponseEntity.ok(new ApiResponse<>("Item deleted successfully",
				null,
				200,
				Instant.now()));
	}

}
