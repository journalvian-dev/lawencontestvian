package com.java.backendtest.service;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.entity.Inventory;
import com.java.backendtest.entity.Item;

@Service
public class InventoryServiceImpl implements InventoryService{

	
	@Autowired
	InventoryRepo inventoryRepo;
	
	@Autowired
	ItemRepo itemRepo;

	@Override
	public Page<InventoryDto> findAll(Pageable pageable) {
		return inventoryRepo.findAll(pageable)
				.map(this::convertEntityToDTO);
	}

	@Override
	public Page<InventoryDto> findByItemId(Long itemId, Pageable pageable) {
		return inventoryRepo.findByItemId(itemId, pageable)
				.map(this::convertEntityToDTO);
	}

	@Override
	public InventoryDto findById(Long id) {
		Inventory entity = inventoryRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("No data found"));
		InventoryDto dto = convertEntityToDTO(entity);
		
		return dto;
	}

	@Override
	public InventoryDto saveInventory(InventoryDtoCreate inventDto) {
		Inventory entity = new Inventory();
		Inventory entResult = new Inventory();
		InventoryDto dto = new InventoryDto();
		
		entity = convertDTOCreatetoEntity(inventDto);		
		entResult = inventoryRepo.save(entity);
		dto = convertEntityToDTO(entResult);
		
		return dto;
	}

	@Transactional
	@Override
	public InventoryDto updateInventory(InventoryDto inventDto) {
		Inventory entity = new Inventory();
		Inventory entResult = new Inventory();
		InventoryDto dto = new InventoryDto();
		
		entity = inventoryRepo.findById(inventDto.getId())
				 .orElseThrow(()-> new RuntimeException("Update data failed because no data found"));

	    Item item = itemRepo.findById(inventDto.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
		entity.setId(inventDto.getId());
		entity.setItem(item);
		entity.setQty(inventDto.getQty());
		entity.setType(inventDto.getType());
		
		entResult = inventoryRepo.save(entity);
		dto = convertEntityToDTO(entResult);
		return dto;
	}

	@Transactional
	@Override
	public void deleteInventory(InventoryDto inventoryDto) {
		Inventory entity = new Inventory();
		entity = inventoryRepo.findById(inventoryDto.getId())
				.orElseThrow(() -> new RuntimeException("Delete failed, no data found"));
		inventoryRepo.delete(entity);
	}
	
	private InventoryDto convertEntityToDTO(Inventory entity) {
		InventoryDto dto = new InventoryDto();
		dto.setId(entity.getId());
		dto.setItemId(entity.getItem().getId());
		dto.setQty(entity.getQty());
		dto.setType(entity.getType());
		
		return dto;
	}
	
	
	private Inventory convertDTOCreatetoEntity (InventoryDtoCreate dto) {
		Inventory entity = new Inventory();
	    Item item = itemRepo.findById(dto.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
	    
		entity.setItem(item);
		entity.setQty(dto.getQty());
		entity.setType(dto.getType());
		
		return entity;
	}

	@Override
	public Long calculateStock(Long itemId) {
		Long stock = inventoryRepo.calculateStock(itemId);
		return stock;
	}

}
