package com.hdfc.midtermproject.grocery.exception;

public class MaximumQuantityForProductExceeded extends Exception{

	@Override
	public String getMessage() {
		
		return "Maximum qunatity of a product cannot exceed six in cart";
	}
	
	
}
