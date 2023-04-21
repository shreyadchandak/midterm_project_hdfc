package com.hdfc.midtermproject.grocery.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.dto.CustomerDTO;
import com.hdfc.midtermproject.grocery.exception.CustomerAlreadyExists;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.service.ICustomerService;

@RestController
@RequestMapping("/customer")
@Validated
public class CustomerController {
	
	@Autowired
	ICustomerService service;
	
	@PostMapping("/user/insertCustomer")
	public CustomerDTO createCustomer(@Valid @RequestBody CustomerDTO c) throws CustomerAlreadyExists {
		
		return service.createCustomer(c);
		
	}
	
	@PutMapping("/customer/updateCustomer")
	public CustomerDTO updateCustomer(@Valid @RequestBody CustomerDTO c) throws CustomerNotFound {
		return service.updateCustomer(c);
	}
	
	@GetMapping("/admin/getCustomerById/{customerId}")
	public CustomerDTO getById(@PathVariable @Positive(message="Enter only positive values") long customerId) throws CustomerNotFound {
		return service.getById(customerId);
	}
	
	@DeleteMapping("/admin/deleteCustomerById/{customerId}")
	public String deleteById(@PathVariable @Positive(message="Enter only positive values") long customerId) throws CustomerNotFound {
		return service.deleteById(customerId);
	}
	
	@GetMapping("/admin/findAll")
	public List<CustomerDTO> findAll() throws CustomerNotFound{
		
		return service.findAll();
	}

}
