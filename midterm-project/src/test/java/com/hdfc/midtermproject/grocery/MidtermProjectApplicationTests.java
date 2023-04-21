package com.hdfc.midtermproject.grocery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdfc.midtermproject.grocery.dto.CartDTO;
import com.hdfc.midtermproject.grocery.dto.CartItemDTO;
import com.hdfc.midtermproject.grocery.dto.CustomerOrderDTO;
import com.hdfc.midtermproject.grocery.entity.Cart;
import com.hdfc.midtermproject.grocery.entity.CartItem;
import com.hdfc.midtermproject.grocery.entity.Customer;
import com.hdfc.midtermproject.grocery.entity.Product;
import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ItemNotFound;
import com.hdfc.midtermproject.grocery.exception.MaximumQuantityForProductExceeded;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.repository.CartItemRepo;
import com.hdfc.midtermproject.grocery.repository.CartRepo;
import com.hdfc.midtermproject.grocery.repository.CustomerRepo;
import com.hdfc.midtermproject.grocery.repository.ProductRepo;
import com.hdfc.midtermproject.grocery.service.ICartService;
import com.hdfc.midtermproject.grocery.service.ICustomerOrderService;
import com.hdfc.midtermproject.grocery.service.ItemRequest;

@SpringBootTest
class MidtermProjectApplicationTests {

	@Autowired
	CartRepo cartRepo;
	@Autowired
	ProductRepo proRepo;
	@Autowired
	CustomerRepo custRepo;
	@Autowired
	ICartService cartService;
	@Autowired
	CartItemRepo cItemRepo;
	@Autowired
	ICustomerOrderService orderService;
	@Test
	public void testAddItem() throws CustomerNotFound, ProductNotFound, ItemNotFound, CartIsEmpty, MaximumQuantityForProductExceeded {
	    // create test data
	    Customer customer = new Customer();
	    customer.setCustomerEmail("test@example.com");
	    Product product = new Product();
	    product.setProductId(99);
	    product.setProductName("Test Product");
	    product.setProductPrice(10.0);
	   
	    product.setStock(true);
	    product.setLive(true);
	    ItemRequest itemRequest = new ItemRequest();
	    itemRequest.setProductId(product.getProductId());
	    itemRequest.setQuantity(2);

	    // mock repository methods
	    when(custRepo.findByCustomerEmail(customer.getCustomerEmail())).thenReturn(customer);
	    when(proRepo.findById(product.getProductId())).thenReturn(Optional.of(product));
	    when(cartRepo.save(any(Cart.class))).thenReturn(new Cart());

	    // call the method
	    CartDTO result = cartService.addItem(itemRequest, customer.getCustomerEmail());

	    // assert the result
	    assertNotNull(result);
	    assertEquals(customer.getCustomerEmail(), result.getCustomer().getCustomerEmail());
	    assertEquals(1, result.getItems().size());
	    Iterator<CartItemDTO> iter = result.getItems().iterator();
	    CartItemDTO cartItemDTO = iter.next();
	    assertEquals(product.getProductId(), cartItemDTO.getProduct().getProductId());
	    assertEquals(itemRequest.getQuantity(), cartItemDTO.getQuantity());
	    assertEquals(product.getProductPrice() * itemRequest.getQuantity(), cartItemDTO.getTotalPrice(), 0.0);

}
	@Test
	void testPlaceOrder() throws Exception {
	    // create a customer
	    Customer customer = new Customer();
	    customer.setCustomerEmail("john.doe@example.com");
	    customer.setCustomerName("John Doe");
	    customer.setCustomerAddress("123 Main St");
	    custRepo.save(customer);

	    // create a product
	    Product product = new Product();
	    product.setProductName("Product test");
	    product.setProductPrice(10.0);
	    product.setStock(true);
	    product.setLive(true);
	    proRepo.save(product);

	    // add the product to the customer's cart
	    Cart cart = new Cart();
	    cart.setCustomer(customer);
	    custRepo.save(customer);
	    CartItem cartItem = new CartItem();
	    cartItem.setProduct(product);
	    cartItem.setQuantity(1);
	    cartItem.setTotalPrice(product.getProductPrice());
	    cartItem.setCart(cart);
	    cItemRepo.save(cartItem);
	    cart.getItems().add(cartItem);
	    cart.setBillAmount(product.getProductPrice());
	    cartRepo.save(cart);
	    customer.setCart(cart);
	    custRepo.save(customer);

	    // place the order
	    CustomerOrderDTO orderDTO = orderService.placeOrder(customer.getCustomerEmail(), 1);

	    // check that the order was created
	    assertNotNull(orderDTO);
	    assertNotNull(orderDTO.getOrderId());
	    assertEquals(customer.getCustomerEmail(), orderDTO.getCustomer().getCustomerEmail());;
	    assertEquals("123 Main St", orderDTO.getDeliveryAddress());
	    assertEquals("PENDING", orderDTO.getOrderStatus());
	    assertEquals(1, orderDTO.getItems().size());
	    assertEquals(product.getProductName(), orderDTO.getItems().iterator().next().getProductName());
	    assertEquals(10.0, orderDTO.getBillAmount(), 0.001);
	}

}
