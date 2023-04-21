package com.hdfc.midtermproject.grocery.exception;

public class ReviewNotGiven extends Exception{
	@Override
	public String getMessage() {
		return "Review not available for the product yet, buy the product and be the first One!";
	}

}
