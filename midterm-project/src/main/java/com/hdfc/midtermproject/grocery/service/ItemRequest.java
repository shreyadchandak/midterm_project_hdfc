package com.hdfc.midtermproject.grocery.service;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ItemRequest {
	@Positive(message="productId should be greater than zero")
	private long productId;

	@Positive(message="productId should be greater than zero")
	
	private int quantity;
	

}
