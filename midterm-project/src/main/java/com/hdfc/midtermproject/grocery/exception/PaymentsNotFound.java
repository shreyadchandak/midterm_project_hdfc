package com.hdfc.midtermproject.grocery.exception;

public class PaymentsNotFound extends Exception{

	@Override
	public String getMessage() {
		return "No Payments Made";
	}
}
