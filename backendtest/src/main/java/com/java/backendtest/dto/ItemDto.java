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
public class ItemDto {
	@NotNull(message = "Id must be filled")
	@Positive(message = "Id must be greater than 0")
	private Long id;
	
	@NotBlank(message = "Name must be filled")
	private String name;
	
	@NotNull(message = "Price must be filled")
	@Positive(message = "Price must be greater than 0")
	private Long price;
	
	private Long stock;
}
