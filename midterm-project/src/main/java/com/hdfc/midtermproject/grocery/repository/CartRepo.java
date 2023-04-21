package com.hdfc.midtermproject.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hdfc.midtermproject.grocery.entity.Cart;
import com.hdfc.midtermproject.grocery.entity.Customer;
@Repository
public interface CartRepo extends JpaRepository<Cart,Long> {

	public Cart findByCustomer(Customer customer);
}