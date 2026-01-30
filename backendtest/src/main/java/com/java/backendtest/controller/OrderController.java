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

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.dto.OrderDtoUpdate;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/findAll")
	public ResponseEntity<Page<OrderDto>> findAll(Pageable pageable){
		Page<OrderDto> dtos = orderService.findAll(pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/findByItemId")
	public ResponseEntity<Page<OrderDto>> findByItemId(Long itemId, Pageable pageable){
		Page<OrderDto> dtos = orderService.findByItemId(itemId, pageable);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/String id/{id}")
	public ResponseEntity<ApiResponse<OrderDto>> findById(@PathVariable String id){
		OrderDto dto = orderService.findById(id);
		return ResponseEntity.ok(new ApiResponse<>("Order item's found",dto));
	}
	
	@PostMapping("/saveOrder")
	public ResponseEntity<ApiResponse<OrderDto>> saveOrder(@Valid @RequestBody OrderDtoCreate orderDto){
		OrderDto dto = orderService.saveOrder(orderDto);
		return ResponseEntity.ok(new ApiResponse<>("Order created successfully",dto));
	}
	
	@PostMapping("/editOrder")
	public ResponseEntity<ApiResponse<OrderDto>> editOrder(@Valid @RequestBody OrderDtoUpdate orderDto){
		OrderDto dto = orderService.updateOrder(orderDto);
		return ResponseEntity.ok(new ApiResponse<>("Order edited successfully",dto));
	}
	
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse<Object>> deleteOrder (@Valid @RequestBody OrderDto orderDto){
		orderService.deleteOrder(orderDto);
		return ResponseEntity.ok(new ApiResponse<>("Order deleted successfully",null));
	}

}
