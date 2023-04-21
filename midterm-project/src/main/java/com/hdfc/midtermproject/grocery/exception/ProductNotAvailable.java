package com.hdfc.midtermproject.grocery.exception;

public class ProductNotAvailable extends Exception{

	long productId;

	public ProductNotAvailable(long productId) {
		super();
		this.productId = productId;
	}
	@Override
	public String getMessage() {
		
		return "Product with id-"+productId+" not available, please remove Item from cart to proceed with order";
	}
	
	
}
