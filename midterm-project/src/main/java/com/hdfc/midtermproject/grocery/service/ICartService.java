package com.hdfc.midtermproject.grocery.service;

import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.dto.CartDTO;
import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ItemNotFound;
import com.hdfc.midtermproject.grocery.exception.MaximumQuantityForProductExceeded;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;

@Service
public interface ICartService {
	public CartDTO addItem(ItemRequest item,String email) throws CustomerNotFound, ProductNotFound, ItemNotFound, CartIsEmpty, MaximumQuantityForProductExceeded ;
	public CartDTO removeItem(long productId, String email) throws ProductNotFound, ItemNotFound, CustomerNotFound ;
	public CartDTO viewCart(String email) throws CustomerNotFound, CartIsEmpty ;
	public CartDTO updateItem(long productId,String email,int quantity) throws ItemNotFound, CartIsEmpty, CustomerNotFound, MaximumQuantityForProductExceeded;
}
