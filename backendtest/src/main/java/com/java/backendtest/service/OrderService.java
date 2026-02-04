package com.java.backendtest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;

public interface OrderService {
	Page<OrderDto> findOrders(Long itemId, Pageable pageable);
	OrderDto findById(String id);
	OrderDto saveOrder(OrderDtoCreate orderDto);
	OrderDto updateOrder(String id, OrderDtoCreate orderDto);
	void deleteOrder (String id);
}
