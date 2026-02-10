package com.java.backendtest.service;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;
import com.java.backendtest.spec.InventorySpecification;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.java.backendtest.dto.InventoryDto;
import com.java.backendtest.dto.InventoryDtoCreate;
import com.java.backendtest.entity.Inventory;
import com.java.backendtest.entity.Item;
import com.java.backendtest.enums.InventoryType;
import com.java.backendtest.exception.DataNotFoundException;
import com.java.backendtest.exception.InsufficientStockException;

@Service
public class InventoryServiceImpl implements InventoryService{

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Page<InventoryDto> findInventories(Long itemId, Pageable pageable) {

        Specification<Inventory> spec =
                Specification.where(
                    InventorySpecification.hasItemId(itemId)
                );

        Page<Inventory> inventories =
                inventoryRepo.findAll(spec, pageable);

        return inventories.map(this::convertEntityToDTO);
    }

    @Override
    public InventoryDto findById(Long id) {
        Inventory entity = inventoryRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Inventory not found"));

        return convertEntityToDTO(entity);
    }

    @Transactional
    @Override
    public InventoryDto createInventory(InventoryDtoCreate dto) {

        validateStock(dto);

        Inventory entity = convertDTOCreatetoEntity(dto);

        return convertEntityToDTO(inventoryRepo.save(entity));
    }

    @Transactional
    @Override
    public InventoryDto updateInventory(Long id, InventoryDtoCreate dto) {

        Inventory entity = inventoryRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Inventory not found"));

        validateStockForUpdate(entity, dto);

        Item item = itemRepo.findById(dto.getItemId())
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        entity.setItem(item);
        entity.setQty(dto.getQty());
        entity.setType(dto.getType());

        return convertEntityToDTO(
                inventoryRepo.save(entity)
        );
    }

    @Transactional
    @Override
    public void deleteInventory(Long id) {

        Inventory entity = inventoryRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Inventory not found"));

        inventoryRepo.delete(entity);
    }
    
    @Override
    public Long calculateStock (Long itemID) {
    	return inventoryRepo.calculateStock(itemID);
    }


    private void validateStock(InventoryDtoCreate dto){

        if(InventoryType.W.equals(dto.getType())){

            Long stock = inventoryRepo.calculateStock(dto.getItemId());

            if(stock == null || stock < dto.getQty()){
                throw new InsufficientStockException();
            }
        }
    }


    private void validateStockForUpdate(Inventory existing, InventoryDtoCreate dto){

        if(InventoryType.W.equals(dto.getType())){

            Long stock = inventoryRepo.calculateStock(dto.getItemId());

            long adjustedStock = stock + existing.getQty();

            if(adjustedStock < dto.getQty()){
                throw new InsufficientStockException();
            }
        }
    }


    private Inventory convertDTOCreatetoEntity (InventoryDtoCreate dto) {

        Item item = itemRepo.findById(dto.getItemId())
                .orElseThrow(() -> new DataNotFoundException("Item not found"));

        Inventory entity = new Inventory();
        entity.setItem(item);
        entity.setQty(dto.getQty());
        entity.setType(dto.getType());

        return entity;
    }

    private InventoryDto convertEntityToDTO(Inventory entity) {

        InventoryDto dto = new InventoryDto();
        dto.setId(entity.getId());
        dto.setItemId(entity.getItem().getId());
        dto.setQty(entity.getQty());
        dto.setType(entity.getType());

        return dto;
    }
    

}
