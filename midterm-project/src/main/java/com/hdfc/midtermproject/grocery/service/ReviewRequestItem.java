package com.hdfc.midtermproject.grocery.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestItem {

	@NotNull(message="Please enter the emailId")
	@Email(message="Please enter the correct email address")
    private String customerEmail;
	@Positive(message="Please enter the positive order Id values")
    private Long OrderId;
	@Positive(message="Please enter the positive product Id values")
    private Long productId;
	@Max(value=5,message="Please enter the rating between 1 to 5")
	@Min(value=1,message="Please enter the rating between 1 to 5")
    private int rating;
}
