package com.java.backendtest.dto;

import com.java.backendtest.enums.InventoryType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryDto {
	@NotNull(message = "Id must be filled")
	@Positive(message = "Id must be greater than 0")
	private Long id;
	
	@NotNull(message = "Item must be filled")
	@Positive(message = "Item Id must be greater than 0")
	private Long itemId;
	
	@NotNull(message = "Quantity must be filled")
	private Long qty;
	
	@Enumerated(EnumType.STRING)
	private InventoryType type;
}
