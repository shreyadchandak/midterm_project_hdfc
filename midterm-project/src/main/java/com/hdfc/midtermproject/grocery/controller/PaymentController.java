package com.hdfc.midtermproject.grocery.controller;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.dto.PaymentDTO;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.service.IPaymentService;

@RestController
@RequestMapping("/payments")
@Validated
public class PaymentController {

	@Autowired
	IPaymentService service;
	
	@PostMapping("/customer/getPaymentHistory/{email}")
	public List<PaymentDTO> setPaymentDetails(@PathVariable @NotNull(message="Enter the EmailId") @Email(message="Enter the correct EmailId")String email) throws CustomerNotFound, ProductNotFound {
		return this.service.paymentHistory(email);
	}

}
