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

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.dto.ItemDtoUpdate;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping("/findAll")
	public ResponseEntity<Page<ItemDto>> findAll(Pageable pageable){
		Page<ItemDto> dtos = itemService.findAll(pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findByNameSpecific")
	public ResponseEntity<Page<ItemDto>> findByNameSpecific (String name, Pageable pageable){
		Page<ItemDto> dtos = itemService.findByNameSpecific(name, pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findByNameNonSpecific")
	public ResponseEntity<Page<ItemDto>> findByNameNonSpecific (String name, Pageable pageable){
		Page<ItemDto> dtos = itemService.findByNameNonSpecific(name, pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<ApiResponse<ItemDto>> findById(@PathVariable Long id) {
		ItemDto dto = itemService.findById(id);
		return ResponseEntity.ok(new ApiResponse<>("Item found",dto));
	}
	
	@PostMapping("/saveItem")
	public ResponseEntity<ApiResponse<ItemDto>>  saveItem (@Valid @RequestBody ItemDtoCreate itemDto) {
		ItemDto dto = itemService.saveItem(itemDto);
		return ResponseEntity.ok(new ApiResponse<>("Item created successfully",dto));
	}
	
	@PostMapping("/editItem")
	public ResponseEntity<ApiResponse<ItemDto>> editItem (@Valid @RequestBody ItemDtoUpdate itemDto) {
		ItemDto tempDto = new ItemDto();
		tempDto.setId(itemDto.getId());
		tempDto.setName(itemDto.getName());
		tempDto.setPrice(itemDto.getPrice());
		
		ItemDto dto = itemService.updateItem(tempDto);
		return ResponseEntity.ok(new ApiResponse<>("Item edited successfully",dto));
	}
	
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@Valid @RequestBody ItemDto itemDto) {
		itemService.deleteItem(itemDto);
		return ResponseEntity.ok(new ApiResponse<>("Item deleted successfully", null));
	}

}
