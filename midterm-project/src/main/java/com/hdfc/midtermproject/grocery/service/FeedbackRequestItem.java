package com.hdfc.midtermproject.grocery.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Validated
public class FeedbackRequestItem {

	@Positive(message="orderId should be greater than zero")
	private long orderId;
	@Email(message="Please enter the correct EmailId")
	private String email;
	
	@Positive(message="Please enter the positive Number for rating")
	@Min(value=1,message="Minimum rating can be one")
	@Max(value=5,message="Maximum rating can be five")
	private int RATE_OUT_OF_FIVE;
	
	
	private String POSITIVE_ASPECTS;
	private String NEGATIVE_ASPECTS;
	
}
