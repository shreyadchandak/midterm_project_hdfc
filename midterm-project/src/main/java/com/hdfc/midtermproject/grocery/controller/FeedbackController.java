package com.hdfc.midtermproject.grocery.controller;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.service.FeedbackRequestItem;
import com.hdfc.midtermproject.grocery.service.IFeedbackService;
import com.hdfc.midtermproject.grocery.service.ReviewRequestItem;

@RestController
@RequestMapping("/feedback")
@Validated
public class FeedbackController {
	
	@Autowired
	IFeedbackService service;
	

	@PostMapping("/customer/giveFeedbackForOrder")
	public String giveFeedback(@Valid @RequestBody FeedbackRequestItem request) throws CustomerNotFound, OrderNotAvailable {
		
		return this.service.giveFeedback(request);
	}
	
	@PostMapping("/customer/giveRatingForProduct")
	public String giveRating(@Valid @RequestBody ReviewRequestItem request) throws CustomerNotFound, ProductNotFound, OrderNotAvailable {
		
		return service.giveRating(request);
		
	}
	
	@GetMapping("/user/showRating/{productId}")
	public  double  showRating(@PathVariable @Positive(message="Please enter the productId values greater than zero")Long productId) throws ProductNotFound {
		return service.showRating(productId);
	}
	
}
