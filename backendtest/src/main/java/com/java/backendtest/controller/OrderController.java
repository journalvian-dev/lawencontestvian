package com.java.backendtest.controller;

import java.time.Instant;

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
	public ResponseEntity<ApiResponse<Page<OrderDto>>> findOrders(@RequestParam(required = false) Long itemId, Pageable pageable){
		Page<OrderDto> data = orderService.findOrders(itemId, pageable);
	    return ResponseEntity.ok(
	            new ApiResponse<>(
	                    "Orders fetched successfully",
	                    data,
	                    200,
	                    Instant.now()
	            )
	    );
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderDto>> findById(@PathVariable String id){
		OrderDto data =  orderService.findById(id);
		
		return ResponseEntity.ok(new ApiResponse<>(
				"Order found",
				data,
				200,
				Instant.now()));
	}
	
	@PostMapping
	public ResponseEntity<ApiResponse<OrderDto>> saveOrder(@Valid @RequestBody OrderDtoCreate orderDto){
		OrderDto data =  orderService.saveOrder(orderDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
				"Order saved successfully",
				data,
				201,
				Instant.now()));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<OrderDto>> editOrder(@PathVariable String id, @Valid @RequestBody OrderDtoCreate orderDto){
		OrderDto data = orderService.updateOrder(id, orderDto);
		return ResponseEntity.ok(new ApiResponse<>(
				"Order edited successfully",
				data,
				200,
				Instant.now()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteOrder (@PathVariable String id){
		orderService.deleteOrder(id);
		return ResponseEntity.ok(new ApiResponse<>(
				"Order deleted successfully",
				null,
				200,
				Instant.now()));
	}

}
