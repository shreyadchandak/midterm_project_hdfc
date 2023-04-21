package com.hdfc.midtermproject.grocery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDTO {

	
	private long paymentId;
	private String paymentMethod="def";
	private String paymentStatus="PENDING";
	private double paymentAmount;
	private long orderId;
	private String email;
}
