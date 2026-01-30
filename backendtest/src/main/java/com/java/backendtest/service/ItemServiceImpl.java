package com.java.backendtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.repo.ItemRepo;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	ItemRepo itemRepo;

	@Override
    public Page<ItemDto> findAll(Pageable pageable) {
        return itemRepo.findAll(pageable)
                .map(this::convertEntityToDTO);
    }
	
	@Override
	public Page<ItemDto> findByNameSpecific(String name, Pageable pageable){
        return itemRepo.findByNameSpecific(name, pageable)
                .map(this::convertEntityToDTO);
	}

	@Override
	public Page<ItemDto> findByNameNonSpecific(String name, Pageable pageable) {
        return itemRepo.findByNameNonSpecific(name, pageable)
                .map(this::convertEntityToDTO);
	}
	
	@Override
	public ItemDto findById(Long id) {
		Item result = itemRepo.findById(id)
				.orElseThrow(()-> new RuntimeException("No data found"));
		ItemDto dto = convertEntityToDTO(result);
		return dto;
	}

	@Override
	public ItemDto saveItem(ItemDtoCreate dtoCreate) {
		Item entity = new Item();
		Item entResult = new Item();
		ItemDto dto = new ItemDto();
		
		entity = convertDTOCreatetoEntity(dtoCreate);
		entResult = itemRepo.save(entity);
		dto = convertEntityToDTO(entResult);
		
		return dto;
	}

	@Transactional
	@Override
	public ItemDto updateItem(ItemDto itemDto) {
		Item entity = new Item();
		Item entResult = new Item();
		ItemDto dto = new ItemDto();
		
		entity = itemRepo.findById(itemDto.getId())
				.orElseThrow(()-> new RuntimeException("Update data failed because no data found"));
		
		entity.setName(itemDto.getName());
		entity.setPrice(itemDto.getPrice());
		
		entResult = itemRepo.save(entity);
		dto = convertEntityToDTO(entResult);	
		
		return dto;
	}

	@Transactional
	@Override
	public void deleteItem(ItemDto itemDto) {
		Item entity = new Item();
		
		entity = itemRepo.findById(itemDto.getId())
				.orElseThrow(()-> new RuntimeException("Delete failed, no data found"));

	    itemRepo.delete(entity);
	}
	
	private ItemDto convertEntityToDTO(Item item) {
		ItemDto dto = new ItemDto();
		dto.setId(item.getId());
		dto.setName(item.getName());
		dto.setPrice(item.getPrice());
		dto.setStock(item.getStock());
		
		return dto;
	}
	
	
	private Item convertDTOCreatetoEntity (ItemDtoCreate dto) {
		Item entity = new Item();
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		
		return entity;
	}

	@Transactional
	@Override
	public void updateItemForOtherController(ItemDto itemDto) {
		Item entity = new Item();
		
		entity = itemRepo.findById(itemDto.getId())
				.orElseThrow(()-> new RuntimeException("Update data failed because no data found"));
		
		entity.setName(itemDto.getName());
		entity.setPrice(itemDto.getPrice());
		entity.setStock(itemDto.getStock());
		
		itemRepo.save(entity);
		
	}



}
