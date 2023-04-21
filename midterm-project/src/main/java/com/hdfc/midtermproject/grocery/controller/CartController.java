package com.hdfc.midtermproject.grocery.controller;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.dto.CartDTO;
import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ItemNotFound;
import com.hdfc.midtermproject.grocery.exception.MaximumQuantityForProductExceeded;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.service.ICartService;
import com.hdfc.midtermproject.grocery.service.ItemRequest;



@RestController
@RequestMapping("/cart")
@Validated
public class CartController {

	@Autowired
	private ICartService service;;
	
	@PostMapping("/customer/addItem/{email}")
	public ResponseEntity<CartDTO> addtoCart(@Valid @RequestBody ItemRequest itemRequest,@PathVariable @NotNull(message="Enter the email Address") @Email(message="Enter the correct email Id") String email) throws CustomerNotFound, ProductNotFound, ItemNotFound, CartIsEmpty, MaximumQuantityForProductExceeded{
		
		CartDTO addItem=this.service.addItem(itemRequest,email);
		return new ResponseEntity<CartDTO>(addItem,HttpStatus.OK);
	}
	
	@DeleteMapping("/customer/removeItem/{productId}/{email}")
     public ResponseEntity<CartDTO> removeFromCart(@PathVariable @Positive(message="Enter only positive values") long productId,@PathVariable @NotNull @Email String email) throws ProductNotFound, ItemNotFound, CustomerNotFound{
		
		CartDTO addItem=this.service.removeItem(productId,email);
		return new ResponseEntity<CartDTO>(addItem,HttpStatus.OK);
	}
	@GetMapping("/customer/viewCart/{email}")
    public ResponseEntity<CartDTO> viewCart(@PathVariable @NotNull(message="Enter the email Address") @Email(message="Enter the correct email Address") String email) throws  CustomerNotFound, CartIsEmpty{
		
		CartDTO addItem=this.service.viewCart(email);
		return new ResponseEntity<CartDTO>(addItem,HttpStatus.OK);
	}
	
	@PutMapping("/customer/updateCartItemQuantity/{productId}/{email}/{newQuantity}")
	public ResponseEntity<CartDTO> updateCart(@PathVariable @Positive(message="The Id should be greater than zero") long productId,@PathVariable @NotNull @Email(message="Enter the corrent email") String email,@PathVariable @Positive(message="Quantity should be greater than zero")int newQuantity) throws ItemNotFound, CartIsEmpty, CustomerNotFound, MaximumQuantityForProductExceeded{
		CartDTO updateItem=this.service.updateItem(productId, email, newQuantity);
		return new ResponseEntity<CartDTO>(updateItem,HttpStatus.OK);
	}
	
}

