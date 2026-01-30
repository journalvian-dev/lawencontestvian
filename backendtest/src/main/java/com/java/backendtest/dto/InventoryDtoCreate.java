package com.java.backendtest.dto;

import com.java.backendtest.enums.InventoryType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InventoryDtoCreate {

	   @NotNull(message = "Item id is required")
	   @Positive(message = "Id must be greater than 0")
	    private Long itemId;

	    @NotNull(message = "Quantity is required")
	    @Positive(message = "Quantity must be greater than 0")
	    private Long qty;

	    @NotNull(message = "Type is required")
	    private InventoryType type;
}
