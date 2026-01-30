package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.dto.OrderDtoUpdate;

public interface OrderService {
	Page<OrderDto> findAll(Pageable pageable);
	Page<OrderDto> findByItemId(Long itemId, Pageable pageable);
	OrderDto findById(String id);
	OrderDto saveOrder(OrderDtoCreate orderDto);
	OrderDto updateOrder(OrderDtoUpdate orderDto);
	void deleteOrder (OrderDto orderDto);
}
