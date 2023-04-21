package com.hdfc.midtermproject.grocery.exception;

public class CustomerAlreadyExists extends Exception {

	@Override
	public String getMessage() {
		return "Customer Already exists";
	}
}
