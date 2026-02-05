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
	public ResponseEntity<Page<ItemDto>> findItems (@RequestParam(required = false) String name, Pageable pageable){
		return ResponseEntity.ok(itemService.findItems(name, pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ItemDto>> findById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>("Item found",itemService.findById(id)));
	}
	
	@PostMapping
	public ResponseEntity<ItemDto>  saveItem (@Valid @RequestBody ItemDtoCreate itemDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(itemService.createItem(itemDto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ItemDto>> editItem (@PathVariable Long id,  @Valid @RequestBody ItemDtoCreate itemDto) {
		return ResponseEntity.ok(new ApiResponse<>("Item edited successfully",itemService.updateItem(id, itemDto)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
		itemService.deleteItem(id);
		return ResponseEntity.ok(new ApiResponse<>("Item deleted successfully", null));
	}

}
