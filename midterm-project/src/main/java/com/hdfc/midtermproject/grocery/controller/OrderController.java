package com.hdfc.midtermproject.grocery.controller;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.dto.CustomerOrderDTO;
import com.hdfc.midtermproject.grocery.dto.OrderHistoryDTO;
import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.ChoiceNotValid;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.NoOrdersAvailable;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotAvailable;
import com.hdfc.midtermproject.grocery.service.ICustomerOrderService;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

	
	@Autowired
	private ICustomerOrderService service;
	
	@PostMapping("/customer/placeorder/{email}/{choiceOfPayment}")
	public CustomerOrderDTO placeOrder(@PathVariable @NotNull(message="Enter the EmailId") @Email(message="Please enter the correct EmailId") String email,@PathVariable @Positive(message="Please enter choicePayment greater than zero")int choiceOfPayment) throws CustomerNotFound,CartIsEmpty, @Positive(message="Enter choiceOption greater than zero")ChoiceNotValid, ProductNotAvailable{
		
		 return this.service.placeOrder(email,choiceOfPayment);
		
	}
	
	@GetMapping("/customer/viewOrders/{email}")
	public List<OrderHistoryDTO> viewOrderHistory(@PathVariable @NotNull(message="Enter the emailId")@Email(message="Enter the correct Email Id") String email) throws CustomerNotFound, NoOrdersAvailable{
		return this.service.viewOrderHistory(email);
	}
	@GetMapping("/customer/viewOrder/{orderId}/{email}")
	public CustomerOrderDTO viewOrder(@PathVariable @Positive(message="Enter the positive OrderId")long orderId,@PathVariable @NotNull(message="Enter the EmailId") @Email(message="Enter the correct EmailId")String email ) throws CustomerNotFound, OrderNotAvailable {
		return this.service.viewOrder(orderId, email);
	}
	
	@PutMapping("/shop/setOrderStatus/{orderId}/{choiceOption}")
	public String setOrderStatusShop(@PathVariable @Positive(message="Enter orderId greater than zero") long orderId,@PathVariable @Positive(message="Enter choiceOption greater than zero")int choiceOption) throws ChoiceNotValid, OrderNotAvailable {
		
		return this.service.setStatusShop(orderId,choiceOption);
	}
	@PutMapping("/agent/setOrderStatus/{orderId}/{choiceOption}")
	public String setOrderStatusAgent(@PathVariable @Positive(message="Enter orderId greater than zero")long orderId,@PathVariable @Positive(message="Enter the choiceOption greater than zero")int choiceOption) throws ChoiceNotValid, OrderNotAvailable {
		
		return this.service.setStatusAgent(orderId,choiceOption);
	}
	
	@GetMapping("/customer/getStatus/{trackingId}")
	public String getOrderStatus(@PathVariable @NotNull(message="Enter the trackingId") String trackingId) throws OrderNotAvailable {
		return this.service.getStatus(trackingId);
	}

	@PutMapping("/customer/cancelOrder/{orderId}/{email}")
	public String cancelOrder(@PathVariable @Positive(message="Enter the orderID greater than zero") long orderId, @PathVariable @NotNull(message="Please the the EmailId") @Email(message="Please enter the correct EmailId") String email) throws CustomerNotFound, OrderNotAvailable {
		return service.cancelOrder(orderId, email);
		
	}
	@PutMapping("/customer/placeOrderAgain/{orderId}/{email}/{choiceOfPayment}")
	public CustomerOrderDTO placeOrderAgain(@PathVariable @Positive(message="Enter the OrderId greater than zero") long orderId,@PathVariable @NotNull@Email(message="Please enter the correct emailId") String email,@PathVariable @Positive(message="Choice should be greater than zero")int choiceOfPayment) throws CustomerNotFound, ChoiceNotValid, OrderNotAvailable {
		
		return service.placeOrderAgain(orderId, email, choiceOfPayment);
	}
	
}
