package com.java.backendtest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {
	
	@NotBlank(message = "Order no is required")
	private String orderNo;
	
	@NotNull(message = "Item Id must be filled")
	@Positive(message = "Item Id must be greater than 0")
	private Long itemId;
	
	@NotNull(message = "Quantity Id must be filled")
	@Positive(message = "Quantity Id must be greater than 0")
	private Long qty;
	
	@NotNull(message = "Price must be filled")
	@Positive(message = "Price must be greater than 0")
	private Long price;
}
