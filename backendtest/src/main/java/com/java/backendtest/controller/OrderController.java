package com.java.backendtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.java.backendtest.dto.OrderDto;
import com.java.backendtest.dto.OrderDtoCreate;
import com.java.backendtest.response.ApiResponse;
import com.java.backendtest.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	OrderService orderService;
	
	@GetMapping
	public ResponseEntity<Page<OrderDto>> findOrders(@RequestParam(required = false) Long itemId, Pageable pageable){
		return ResponseEntity.ok(orderService.findOrders(itemId, pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderDto>> findById(@PathVariable String id){
		return ResponseEntity.ok(new ApiResponse<>("Order item's found",orderService.findById(id)));
	}
	
	@PostMapping
	public ResponseEntity<OrderDto> saveOrder(@Valid @RequestBody OrderDtoCreate orderDto){
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.saveOrder(orderDto));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderDto>> editOrder(@PathVariable String id, @Valid @RequestBody OrderDtoCreate orderDto){
		return ResponseEntity.ok(new ApiResponse<>("Order edited successfully", orderService.updateOrder(id, orderDto)));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteOrder (@PathVariable String id){
		orderService.deleteOrder(id);
		return ResponseEntity.ok(new ApiResponse<>("Order deleted successfully",null));
	}

}
