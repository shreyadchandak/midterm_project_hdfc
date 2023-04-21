package com.hdfc.midtermproject.grocery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.dto.PaymentDTO;
import com.hdfc.midtermproject.grocery.entity.Customer;
import com.hdfc.midtermproject.grocery.entity.Payment;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.repository.CustomerRepo;
import com.hdfc.midtermproject.grocery.repository.PaymentRepo;

@Service
public class PaymentServiceImp implements IPaymentService{

	@Autowired
	PaymentRepo payRepo;
	@Autowired
	CustomerRepo custRepo;
	public List<PaymentDTO> paymentHistory(String email) throws CustomerNotFound, ProductNotFound
	{
		Customer customer=custRepo.findByCustomerEmail(email);
		if(customer==null) {
			throw new CustomerNotFound();
		}
		if(payRepo.findByEmail(email)==null) {
			throw new ProductNotFound();
		}
	    return toDTO(payRepo.findByEmail(email));	
	}

public List<PaymentDTO> toDTO(List<Payment> payments) {
	List<Payment> payHistory=payments;
	List<PaymentDTO> payHistoryDTO=new ArrayList<>();
	for(Payment payment:payHistory) {
		PaymentDTO p=new PaymentDTO();
		p.setEmail(payment.getEmail());
		p.setOrderId(payment.getOrderId());
		p.setPaymentAmount(payment.getPaymentAmount());
		p.setPaymentId(payment.getOrderId());
		p.setPaymentStatus(payment.getPaymentStatus());
		p.setPaymentMethod(payment.getPaymentMethod());
		payHistoryDTO.add(p);
	}
	return payHistoryDTO;
}
}