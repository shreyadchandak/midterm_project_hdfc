package com.hdfc.midtermproject.grocery.service;

import java.util.HashSet;
import java.util.Set;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.dto.CartDTO;
import com.hdfc.midtermproject.grocery.dto.CartItemDTO;
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


@Service
public class CartServiceImp implements ICartService{

	@Autowired
	private CustomerRepo custRepo;
	@Autowired
	private ProductRepo proRepo;
	
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private CartItemRepo itemRepo;
	
	
	@Autowired
	private ICustomerService custService;
	
	@Autowired
	private IProductService proService;
	
	
	
	
		public CartDTO addItem(ItemRequest item, String email) throws CustomerNotFound, ProductNotFound, ItemNotFound, CartIsEmpty, MaximumQuantityForProductExceeded {
		    long productId = item.getProductId();
		    int productQuantity = item.getQuantity();
		    Customer customer = this.custRepo.findByCustomerEmail(email);

		    if (customer == null) {
		        throw new CustomerNotFound();
		    }

		    Product product = this.proRepo.findById(productId).orElse(null);

		    if (product == null) {
		        throw new ProductNotFound();
		    }

		    if (!(product.isStock()&& product.isLive())) {
		        throw new OpenApiResourceNotFoundException("Product Out of Stock");
		    }
		    if(item.getQuantity()>6)
		    {
		    	throw new MaximumQuantityForProductExceeded();
		    }

		    Cart cart = customer.getCart();
		    if (cart == null) {
		        cart = new Cart();
		        cart.setCustomer(customer);
		        customer.setCart(cart);
		    }
		    for(CartItem aItem:customer.getCart().getItems()) {
		    	if(aItem.getProduct().getProductId()==item.getProductId()) {
		    	 return	updateItem(aItem.getProduct().getProductId(),customer.getCustomerEmail(),aItem.getQuantity()+item.getQuantity());
		    	}
		    }

		    Set<CartItem> items = cart.getItems();
		    boolean itemFound = false;
		    for (CartItem cartItem : items) {
		        if (cartItem.getProduct().getProductId() == product.getProductId()) {
		            cartItem.setQuantity(productQuantity);
		            cartItem.setTotalPrice(product.getProductPrice() * productQuantity);
		            itemFound = true;
		            break;
		        }
		    }
		    if (!itemFound) {
		        CartItem cartItem = new CartItem();
		        cartItem.setProduct(product);
		        cartItem.setQuantity(productQuantity);
		        cartItem.setTotalPrice(product.getProductPrice() * productQuantity);
		        cartItem.setCart(cart);
		        items.add(cartItem);
		    }

		    Set<CartItem> i=cart.getItems();
		    double billAmount=0.0;
		    for(CartItem j:i) {
		    
		    	 billAmount+=j.getTotalPrice();
		    }
		    cart.setBillAmount(billAmount);
		    Cart savedCart = this.cartRepo.save(cart);
		   
		    return toDTO(savedCart);
		}

		public CartDTO removeItem(long productId, String email) throws ProductNotFound, ItemNotFound, CustomerNotFound {
		    
			double billAmountSub=0.0;
			Customer customer = custRepo.findByCustomerEmail(email);
			if (customer == null) {
		        throw new CustomerNotFound();
		    }

		    

		    Product product = proRepo.findById(productId).orElse(null);
		    if (product == null) {
		        throw new ProductNotFound();
		    }

		    Cart cart = customer.getCart();
		    

		    Set<CartItem> cartItems = cart.getItems();
		    boolean itemRemoved = false;
		    for (CartItem item : cartItems) {
		        if (item.getProduct().getProductId() == productId) {
		        	billAmountSub=item.getTotalPrice();
		            cartItems.remove(item);
		            itemRemoved=true;
		            itemRepo.deleteById(item.getCartItemId());
		            break;
		        }
		    }
		    if(!itemRemoved) {
		    	throw new ItemNotFound();
		    }
		   
		   customer.getCart().setBillAmount(customer.getCart().getBillAmount()-billAmountSub);
		   custRepo.save(customer);
		   return toDTO(cart);
		    
		   
		}
		
		public CartDTO updateItem(long productId,String email,int quantity) throws ItemNotFound, CartIsEmpty, CustomerNotFound, MaximumQuantityForProductExceeded{
			
			Customer customer = custRepo.findByCustomerEmail(email);
			if (customer == null) {
		        throw new CustomerNotFound();
		    }
			
			if(customer.getCart().getItems().isEmpty()) {
				throw new CartIsEmpty();
			}
			
			if(quantity>6) {
				throw new MaximumQuantityForProductExceeded();
			}
			CartItem item=new CartItem();
			boolean flag=false;
			for(CartItem cartItem:customer.getCart().getItems()) {
				if(cartItem.getProduct().getProductId()==productId) {
				flag=true;	
				
				int prevQuantity=cartItem.getQuantity();
				cartItem.setQuantity(quantity);
				cartItem.setTotalPrice(quantity*cartItem.getProduct().getProductPrice());
				cartItem.getCart().setBillAmount(cartItem.getCart().getBillAmount()-prevQuantity*cartItem.getProduct().getProductPrice()+quantity*cartItem.getProduct().getProductPrice());
				cartRepo.save(cartItem.getCart());
				itemRepo.save(cartItem);
				customer.setCart(cartItem.getCart());
				custRepo.save(customer);
				item=itemRepo.getById(cartItem.getCartItemId());
				}
			}
			
			if(!flag) {
				throw new ItemNotFound();
			}
			return toDTO(item.getCart());
		}
			
			
		
		
		public CartDTO viewCart(String email) throws CustomerNotFound, CartIsEmpty {
			
			Customer customer=custRepo.findByCustomerEmail(email);
			if(customer==null)
			{
				throw new CustomerNotFound();
			}
			
			Cart cart=customer.getCart();
			if(cart==null || cart.getItems().isEmpty()) {
				throw new CartIsEmpty();
			}
			
			return toDTO(cart);
			
		}
		
	
		
		

		public CartDTO toDTO(Cart cart) {
		    CartDTO cartDTO = new CartDTO();
		    cartDTO.setCartId(cart.getCartId());
		    cartDTO.setBillAmount(cart.getBillAmount());
		    
		    Set<CartItemDTO> cartItemDTOs = new HashSet<>();
		    for (CartItem cartItem : cart.getItems()) {
		        CartItemDTO cartItemDTO = new CartItemDTO();
		        cartItemDTO.setCartItemId(cartItem.getCartItemId());
		        cartItemDTO.setProduct(proService.toDTO(cartItem.getProduct()));
		        cartItemDTO.setQuantity(cartItem.getQuantity());
		        cartItemDTO.setTotalPrice(cartItem.getTotalPrice());
		        cartItemDTOs.add(cartItemDTO);
		        
		    }
		    
		    cartDTO.setCustomer(custService.toDTO(cart.getCustomer()));
		    cartDTO.setItems(cartItemDTOs);
		    
		    return cartDTO;
		}

	
	
	
	

	
}

