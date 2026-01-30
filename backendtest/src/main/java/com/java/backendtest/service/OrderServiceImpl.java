package com.java.backendtest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.dto.OrderDtoUpdate;
import com.java.backendtest.entity.Item;
import com.java.backendtest.entity.Order;
import com.java.backendtest.repo.ItemRepo;
import com.java.backendtest.repo.OrderRepo;

@Service
public class OrderServiceImpl implements OrderService{
	
	private static final String ORDER_PREFIX = "O"; 
	
	@Autowired
	OrderRepo orderRepo;
	
	@Autowired
	ItemRepo itemRepo;

	@Override
	public Page<OrderDto> findAll(Pageable pageable) {
		return orderRepo.findAll(pageable)
				.map(this::convertEntityToDTO);
	}

	@Override
	public Page<OrderDto> findByItemId(Long itemId, Pageable pageable) {
		return orderRepo.findByItemId(itemId, pageable)
				.map(this::convertEntityToDTO);
	}

	@Override
	public OrderDto findById(String id) {
		Order order = orderRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("No data found"));
		OrderDto dto = convertEntityToDTO(order);
		return dto;
	}

	@Override
	public OrderDto saveOrder(OrderDtoCreate orderDto) {
		Order entity = new Order();
		Order enResult = new Order();
		OrderDto dto = new OrderDto();
		
	    Item item = itemRepo.findById(orderDto.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
		
	    entity.setOrderNo(generateNextOrderNo());
		entity.setItem(item);
		entity.setPrice(item.getPrice());
		entity.setQty(orderDto.getQty());
		
		enResult = orderRepo.save(entity);
		System.out.println("enResult >> " + enResult);
		dto = convertEntityToDTO(enResult);
		
		return dto;
	}

	@Override
	public OrderDto updateOrder(OrderDtoUpdate orderDto) {
		Order entity = new Order();
		Order enResult = new Order();
		OrderDto dto = new OrderDto();
		
		entity = orderRepo.findById(orderDto.getOrderNo())
				.orElseThrow(()-> new RuntimeException("Update data failed because no data found"));

		Item item = itemRepo.findById(orderDto.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
		
		entity.setOrderNo(orderDto.getOrderNo());
		entity.setItem(item);
		entity.setPrice(item.getPrice());
		entity.setQty(orderDto.getQty());
		
		enResult = orderRepo.save(entity);
		
		dto = convertEntityToDTO(enResult);
		
		return dto;
	}

	@Override
	public void deleteOrder(OrderDto orderDto) {
		Order entity = new Order();
		entity = orderRepo.findById(orderDto.getOrderNo())
				.orElseThrow(() -> new RuntimeException("Delete failed, no data found"));
		orderRepo.delete(entity);
	}
	
	private OrderDto convertEntityToDTO(Order order) {
		OrderDto dto = new OrderDto();
		dto.setOrderNo(order.getOrderNo());
		dto.setItemId(order.getItem().getId());
		dto.setQty(order.getQty());
		dto.setPrice(order.getPrice());
		return dto;
	}
	
	
	private Order convertDTOCreatetoEntity (OrderDtoCreate dto) {
		Order entity = new Order();
	    Item item = itemRepo.findById(dto.getItemId())
	            .orElseThrow(() -> new RuntimeException("Item not found"));
		
		entity.setItem(item);
		entity.setPrice(item.getPrice());
		entity.setQty(dto.getQty());
		
		return entity;
	}
	
	   private String generateNextOrderNo() {

	        Page<String> page = orderRepo.findLastOrderNo(PageRequest.of(0, 1));

	        if (page.isEmpty()) {
	            return ORDER_PREFIX + "1";
	        }

	        String lastOrderNo = page.getContent().get(0); // contoh: O9
	        int lastNumber = Integer.parseInt(lastOrderNo.substring(1));

	        return ORDER_PREFIX + (lastNumber + 1);
	    }

}
