package com.java.backendtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.entity.Inventory;
import com.java.backendtest.entity.Item;
import com.java.backendtest.entity.Order;
import com.java.backendtest.enums.InventoryType;
import com.java.backendtest.exception.DataNotFoundException;
import com.java.backendtest.exception.InsufficientStockException;
import com.java.backendtest.repo.InventoryRepo;
import com.java.backendtest.repo.ItemRepo;
import com.java.backendtest.repo.OrderRepo;
import com.java.backendtest.spec.OrderSpecification;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
    OrderRepo orderRepo;
	
	@Autowired
    ItemRepo itemRepo;
	
	@Autowired
    InventoryRepo inventoryRepo;

    @Override
    public Page<OrderDto> findOrders(Long itemId, Pageable pageable) {

        Specification<Order> spec =
                Specification.where(
                    OrderSpecification.hasItemId(itemId)
                );

        Page<Order> orders =
                orderRepo.findAll(spec, pageable);

        return orders.map(this::convertToDto);
    }

    @Override
    public OrderDto findById(String id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Order not found"));

        return convertToDto(order);
    }

    @Transactional
    @Override
    public OrderDto saveOrder(OrderDtoCreate dto) {

        Item item = itemRepo.findById(dto.getItemId())
                .orElseThrow(() ->
                        new DataNotFoundException("Item not found"));

        validateStock(item.getId(), dto.getQty());

        Order order = new Order();
        order.setOrderNo(generateNextOrderNo());
        order.setItem(item);
        order.setPrice(item.getPrice());
        order.setQty(dto.getQty());

        Order savedOrder = orderRepo.save(order);

        createInventoryWithdraw(item, dto.getQty());

        return convertToDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto updateOrder(String id, OrderDtoCreate dto) {

        Order existing = orderRepo.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Order not found"));

        Item item = itemRepo.findById(dto.getItemId())
                .orElseThrow(() ->
                        new DataNotFoundException("Item not found"));

        validateStockForUpdate(item.getId(), existing.getQty(), dto.getQty());

        Long oldQty = existing.getQty();
        
        existing.setItem(item);
        existing.setPrice(item.getPrice());
        existing.setQty(dto.getQty());

        Order updated = orderRepo.save(existing);

        recreateInventoryWithdraw(existing, oldQty);

        return convertToDto(updated);
    }

    @Transactional
    @Override
    public void deleteOrder(String id) {

        Order order = orderRepo.findById(id)
                .orElseThrow(() ->
                        new DataNotFoundException("Order not found"));
        
        createInventoryRestock(
                order.getItem(),
                order.getQty()
        );

        orderRepo.delete(order);
    }

    private void validateStock(Long itemId, Long qty){

        Long stock = inventoryRepo.calculateStock(itemId);

        if(stock == null || stock < qty){
            throw new InsufficientStockException();
        }
    }


    private void validateStockForUpdate(
            Long itemId,
            Long oldQty,
            Long newQty){

        Long stock = inventoryRepo.calculateStock(itemId);

        long adjusted = stock + oldQty;

        if(adjusted < newQty){
            throw new InsufficientStockException();
        }
    }

    private void createInventoryWithdraw(Item item, Long qty){

        Inventory inv = new Inventory();
        inv.setItem(item);
        inv.setQty(qty);
        inv.setType(InventoryType.W);

        inventoryRepo.save(inv);
    }

    private void createInventoryRestock(Item item, Long qty){

        Inventory inv = new Inventory();
        inv.setItem(item);
        inv.setQty(qty);
        inv.setType(InventoryType.T);

        inventoryRepo.save(inv);
    }

    private void recreateInventoryWithdraw(Order order, Long oldQty){

        createInventoryRestock(order.getItem(), oldQty);
        createInventoryWithdraw(order.getItem(), order.getQty());
    }

    private OrderDto convertToDto(Order order){

        OrderDto dto = new OrderDto();
        dto.setOrderNo(order.getOrderNo());
        dto.setItemId(order.getItem().getId());
        dto.setQty(order.getQty());
        dto.setPrice(order.getPrice());

        return dto;
    }

    private String generateNextOrderNo(){

        Page<String> page =
                orderRepo.findLastOrderNo(PageRequest.of(0,1));

        if(page.isEmpty()){
            return "O1";
        }

        String last = page.getContent().get(0);
        int number = Integer.parseInt(last.substring(1));

        return "O" + (number + 1);
    }
}
