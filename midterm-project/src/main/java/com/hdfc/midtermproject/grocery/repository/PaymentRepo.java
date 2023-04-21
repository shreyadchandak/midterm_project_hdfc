package com.hdfc.midtermproject.grocery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hdfc.midtermproject.grocery.entity.Payment;
@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
	public Payment findByOrderId(long id);
	public List<Payment> findByEmail(String email);

}
