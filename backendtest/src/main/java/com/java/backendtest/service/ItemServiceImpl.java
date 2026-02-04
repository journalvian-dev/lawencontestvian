package com.java.backendtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.backendtest.dto.ItemDto;
import com.java.backendtest.dto.ItemDtoCreate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.exception.DataNotFoundException;
import com.java.backendtest.repo.ItemRepo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
    ItemRepo itemRepo;

    @Override
    public Page<ItemDto> findItems(String name, Pageable pageable) {

    	Page <Item> items = (name == null || name.isBlank()) 
    		 ? itemRepo.findAll(pageable) 
    	     : itemRepo.findByName(name, pageable);

    	  return items.map(this::convertEntityToDTO);
    }

    @Override
    public ItemDto findById(Long id) {

        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        return convertEntityToDTO(item);
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemDtoCreate dtoCreate) {

        Item entity = convertDTOCreatetoEntity(dtoCreate);

        return convertEntityToDTO(
                itemRepo.save(entity)
        );
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long id, ItemDtoCreate itemDto) {

        Item entity = itemRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        entity.setName(itemDto.getName());
        entity.setPrice(itemDto.getPrice());

        return convertEntityToDTO(
                itemRepo.save(entity)
        );
    }

    @Transactional
    @Override
    public void deleteItem(Long id) {

        Item entity = itemRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        itemRepo.delete(entity);
    }


    private ItemDto convertEntityToDTO(Item item) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());

        return dto;
    }

    private Item convertDTOCreatetoEntity(ItemDtoCreate dto) {

        Item entity = new Item();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());

        return entity;
    }
}

