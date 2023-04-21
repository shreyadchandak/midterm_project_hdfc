package com.hdfc.midtermproject.grocery.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.dto.PaymentDTO;
import com.hdfc.midtermproject.grocery.entity.Payment;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;

@Service
public interface IPaymentService{
	
	public List<PaymentDTO> paymentHistory(String email) throws CustomerNotFound, ProductNotFound;

}
