package com.hdfc.midtermproject.grocery.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.dto.CustomerOrderDTO;
import com.hdfc.midtermproject.grocery.dto.OrderHistoryDTO;
import com.hdfc.midtermproject.grocery.dto.OrderItemDTOPretty;
import com.hdfc.midtermproject.grocery.entity.Cart;
import com.hdfc.midtermproject.grocery.entity.CartItem;
import com.hdfc.midtermproject.grocery.entity.Customer;
import com.hdfc.midtermproject.grocery.entity.CustomerOrder;
import com.hdfc.midtermproject.grocery.entity.OrderItem;
import com.hdfc.midtermproject.grocery.entity.Payment;
import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.ChoiceNotValid;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.NoOrdersAvailable;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotAvailable;
import com.hdfc.midtermproject.grocery.repository.CartItemRepo;
import com.hdfc.midtermproject.grocery.repository.CustomerRepo;
import com.hdfc.midtermproject.grocery.repository.OrderItemRepo;
import com.hdfc.midtermproject.grocery.repository.OrderRepo;
import com.hdfc.midtermproject.grocery.repository.PaymentRepo;

@Service
public class OrderServiceImp implements ICustomerOrderService {
	

@Autowired
CustomerRepo custRepo;	

@Autowired
OrderRepo ordRepo;

@Autowired 
OrderItemRepo itemRepo;

@Autowired
IProductService proService;

@Autowired
ICustomerService custService;

@Autowired
CartItemRepo cItemRepo;

@Autowired
PaymentRepo payRepo;	


	public CustomerOrderDTO placeOrder(String email,int choice) throws CustomerNotFound, CartIsEmpty,ChoiceNotValid, ProductNotAvailable {
	    Customer customer = custRepo.findByCustomerEmail(email);
	    if(customer == null) {
	        throw new CustomerNotFound();
	    }
	    Cart cart = customer.getCart();
	    if(cart == null || cart.getItems().isEmpty()) {
	        throw new CartIsEmpty();
	    }
	    for(CartItem cartItem:customer.getCart().getItems()) {
	    	if(!(cartItem.getProduct().isStock() && cartItem.getProduct().isLive())){
	    		throw new ProductNotAvailable(cartItem.getProduct().getProductId());
	    	}
	    }
	    Set<CartItem> cartItems=cart.getItems(); 
	    Payment p=new Payment();   
	    Payment payment=payRepo.save(p);
		{
			switch(choice) {
			case 1: payment.setPaymentMethod("Credit/Debit Cards");
			        payment.setPaymentStatus("PAID");
			        payRepo.save(payment);
			        break;
			case 2:payment.setPaymentMethod("UPI");
			       payment.setPaymentStatus("PAID");
			       payRepo.save(payment);
			       break;
			case 3:payment.setPaymentMethod("COD");
			       payRepo.save(payment);
			       break;
			default :payRepo.deleteById(payment.getPaymentId());
				     throw new ChoiceNotValid();
			}
	}
	    
	    CustomerOrder order=new CustomerOrder();
	    order.setCustomer(customer);
	    payment.setEmail(order.getCustomer().getCustomerEmail());
	    order.setDeliveryAddress(customer.getCustomerAddress());
	    order.setOrderDate( LocalDateTime.now());
	    order.setOrderStatus("PENDING");
	    Set<OrderItem> orderItems = new HashSet<>();
	    double totalAmount=0.0;
	    for(CartItem cartItem: cartItems) {
	        OrderItem orderItem = new OrderItem();
	        orderItem.setProduct(cartItem.getProduct());
	        orderItem.setQuantity(cartItem.getQuantity());
	        orderItem.setTotalPrice(cartItem.getTotalPrice());
	        orderItem.setOrder(order);
	        orderItems.add(orderItem);
	        totalAmount += cartItem.getTotalPrice();
	        cart.getItems().remove(cartItem);
	        cItemRepo.deleteById(cartItem.getCartItemId());
	    }
	    order.setExpectedDelivery(LocalDateTime.now().plusHours(2));
	    order.setOrderItems(orderItems);
	    order.setBillAmount(totalAmount);
	    payment.setPaymentAmount(totalAmount);
	    ordRepo.save(order);
	    payment.setOrderId(order.getOrderId());
	    order.setTrackingId(order.getOrderId()+""+customer.getCustomerEmail());
	    cart.setItems(new HashSet<>());
	    cart.setBillAmount(0.0);
	    custRepo.save(customer);
	    ordRepo.save(order);

	    return toDTO(order);
	}
	public CustomerOrderDTO viewOrder(long id,String email) throws CustomerNotFound, OrderNotAvailable {
		
		Customer customer=custRepo.findByCustomerEmail(email);
		if(customer==null) {
			throw new CustomerNotFound();
		}

		List<CustomerOrder> orders=customer.getOrders();
		for(CustomerOrder order:orders) {
			if(order.getOrderId()==id) {
				return toDTO(order);
			}
		}
		throw new OrderNotAvailable();
		
		
	}
	
	public String cancelOrder(long id,String email) throws CustomerNotFound, OrderNotAvailable {
		
		Customer customer=custRepo.findByCustomerEmail(email);
		if(customer==null) {
			throw new CustomerNotFound();
		}
	    boolean flag=false;
	    String status=null;
		List<CustomerOrder> orders=customer.getOrders();
		for(CustomerOrder order:orders) {
			if(order.getOrderId()==id) {
				flag=true;
				status=order.getOrderStatus();
				if(order.getOrderStatus().equals("PENDING") ||order.getOrderStatus().equals("CONFIRMED") ||order.getOrderStatus().equals("PACKING")) {
			    order.setOrderStatus("CANCELLED");
			    payRepo.findByOrderId(id).setPaymentStatus("REFUNDED");
				ordRepo.save(order);
			    return "Order Cancelled Successfully";
			}
		}
	}        
		        
		if(flag==false) 
		{
				throw new OrderNotAvailable();
	    }
				
		return "Order cannot be cancelled at "+ status +" stage";

	}
	
	public String setStatusShop(long id,int choice) throws ChoiceNotValid, OrderNotAvailable {
		CustomerOrder order=ordRepo.getById(id);
		if(order==null) {
			throw new OrderNotAvailable();
		}
		switch(choice) {
		case 1:if(order.getOrderStatus().equals("PENDING"))
		       {
			   order.setOrderStatus("CONFIRMED");
		       ordRepo.save(order);
		       }else return "DISABLED CHOICE";
		       break;
		case 2:if(order.getOrderStatus().equals("CONFIRMED"))
		       {
			    order.setOrderStatus("PACKING");
			       ordRepo.save(order);
		       }else return "DISABLED CHOICE";
		       break;
		       
		case 3:if(order.getOrderStatus().equals("PACKING")) 
		       {
			   order.setOrderStatus("ON THE WAY");
		       ordRepo.save(order);
		       }else return "DISABLED CHOICE";
		       break;
		default:throw new ChoiceNotValid();
		
		}
		return "Updated Order Status to "+order.getOrderStatus();
	}

	public String setStatusAgent(long id,int choice) throws ChoiceNotValid, OrderNotAvailable {
		CustomerOrder order=ordRepo.getById(id);
		if(order==null) {
			throw new OrderNotAvailable();
		}
		
		
		if(order.getOrderStatus().equals("ON THE WAY")){ 
		
			switch(choice) {
			
			case 1:
				   order.setOrderStatus("DELIVERED");
				   order.setFeedback("ENABLE");
				   Payment payment=payRepo.findByOrderId(id);
				   payment.setPaymentStatus("PAID");
				   payRepo.save(payment);
			       ordRepo.save(order);
			       break;
			       
			case 2:if(!(order.getOrderStatus().equals("DELIVERED"))) {
				   order.setOrderStatus("RETURNED DUE TO ADDRESS NOT FOUND");
				   Payment payment1=payRepo.findByOrderId(id);
				   if(payment1.getPaymentMethod().equals("COD")){
					   payment1.setPaymentStatus("CLEAR");
				   }else payment1.setPaymentStatus("REFUNDED");
				     payRepo.save(payment1);
			         ordRepo.save(order);
			        }else return("DISABLED CHOICE");
			       break;
			case 3:if(!(order.getOrderStatus().equals("DELIVERED"))) {
				   order.setOrderStatus("RETURNED DUE TO NO ONE TO RECIEVE ORDER");
                   Payment payment1=payRepo.findByOrderId(id);
				   if(payment1.getPaymentMethod().equals("COD")){
					   payment1.setPaymentStatus("CLEAR");
				   }else payment1.setPaymentStatus("REFUNDED");
			       payRepo.save(payment1);
			       ordRepo.save(order);
			       
			       }else return("DISABLED CHOICE");
			      break;
			case 4:if(!(order.getOrderStatus().equals("DELIVERED"))) {
				   order.setOrderStatus("RETURNED DUE TO CANNOT CONTACT THE CUSTOMER");
                   Payment payment1=payRepo.findByOrderId(id);
				   if(payment1.getPaymentMethod().equals("COD")){
					   payment1.setPaymentStatus("CLEAR");
					   payRepo.save(payment1);
				   }else payRepo.getById(id).setPaymentStatus("REFUNDED");
			       
			       ordRepo.save(order);
			       }else return("DISABLED CHOICE");
			       break;
			
			default:throw new ChoiceNotValid();
			}
			
		
		}
		else
		{
			return "cannot set the Status now, you are not permitted";
		}
		return "Updated Order Status to "+order.getOrderStatus();

		
	}


	
	
	public String getStatus(String trackingId) throws OrderNotAvailable {
		CustomerOrder order=ordRepo.findByTrackingId(trackingId);
		if(order==null) {
			throw new OrderNotAvailable();
		}
		if(!(order.getOrderStatus().equals("DELIVERED")) && !(order.getOrderStatus().equals("CANCELLED")) && !(order.getOrderStatus().equals( "RETURNED DUE TO ADDRESS NOT FOUND"))
			&&!(order.getOrderStatus().equals("RETURNED DUE TO NO ONE TO RECIEVE ORDER"))&&!(order.getOrderStatus().equals("RETURNED DUE TO CANNOT CONTACT THE CUSTOMER")))
			
			{
			long min=ChronoUnit.MINUTES.between(LocalDateTime.now(),order.getExpectedDelivery());
			if(min>0) {
			return order.getOrderStatus()+", Order will be deliverd in "+ChronoUnit.MINUTES.between(LocalDateTime.now(),order.getExpectedDelivery())+" minutes";
			}
	}

		return order.getOrderStatus();

	}
	
	public CustomerOrderDTO placeOrderAgain(long id,String email,int choice) throws CustomerNotFound, ChoiceNotValid, OrderNotAvailable {
		
		Customer customer=custRepo.findByCustomerEmail(email);
		if(customer==null) {
			throw new CustomerNotFound();
		}
	    Payment p=new Payment();   
	    Payment payment=payRepo.save(p);
		{
			switch(choice) {
			case 1: payment.setPaymentMethod("Credit/Debit Cards");
			        payment.setPaymentStatus("PAID");
			        payRepo.save(payment);
			        break;
			case 2:payment.setPaymentMethod("UPI");
			       payment.setPaymentStatus("PAID");
			       payRepo.save(payment);
			       break;
			case 3:payment.setPaymentMethod("COD");
			       payRepo.save(payment);
			       break;
			default :payRepo.deleteById(payment.getPaymentId());
				     throw new ChoiceNotValid();
			}
	}
		CustomerOrder order=new CustomerOrder();
		List<CustomerOrder> orders=customer.getOrders();
		boolean flag=false;
		for(CustomerOrder o:orders) {
			if(o.getOrderId()==id) {
				flag=true;
				order.setBillAmount(o.getBillAmount());
				order.setCustomer(customer);
				order.setDeliveryAddress(o.getDeliveryAddress());
				order.setOrderDate(LocalDateTime.now());
				order.setExpectedDelivery(LocalDateTime.now());
				order.setExpectedDelivery(LocalDateTime.now().plusHours(2));
				order.setOrderStatus("PENDING");
				ordRepo.save(order);
				order.setTrackingId(order.getOrderId()+""+order.getCustomer().getCustomerEmail());
				Set<OrderItem> orderItems=new HashSet<>();
				for(OrderItem ois:o.getOrderItems())
				{
					OrderItem nois=new OrderItem();
					nois.setOrder(order);
					nois.setProduct(ois.getProduct());
					nois.setQuantity(ois.getQuantity());
					nois.setTotalPrice(ois.getTotalPrice());
					orderItems.add(nois);
				}
			
				order.setOrderItems(orderItems);
				custRepo.save(customer);
				ordRepo.save(order);
				
			}
		}
		if(flag==false) {
			throw new OrderNotAvailable();
		}
		return toDTO(order);
	}
	

    public List<OrderHistoryDTO> viewOrderHistory(String email) throws CustomerNotFound, NoOrdersAvailable{
    	Customer customer = custRepo.findByCustomerEmail(email);
	    if(customer == null) {
	        throw new CustomerNotFound();
	    }
	    
	    List<CustomerOrder> orders=customer.getOrders();
	    if(orders==null) {
	    	throw new NoOrdersAvailable();
	    }
    	List<OrderHistoryDTO> ordersHistoryDTO=new ArrayList<>();
    	for(CustomerOrder order:orders) {
    		ordersHistoryDTO.add(toHistoryDTO(order));
    	}
    	return ordersHistoryDTO;
    	
    }
	public OrderHistoryDTO toHistoryDTO(CustomerOrder order) {

	    OrderHistoryDTO orderDTO = new OrderHistoryDTO();
	    orderDTO.setOrderId(order.getOrderId());
	    orderDTO.setOrderDate(order.getOrderDate());
	    orderDTO.setBillAmount(order.getBillAmount());
	    orderDTO.setOrderStatus(order.getOrderStatus());
	    orderDTO.setDeliveryAddress(order.getDeliveryAddress());
	    orderDTO.setFeedback(order.getFeedback());
	    
	    
	    Set<OrderItemDTOPretty> orderItemDTOs = new HashSet<>();
	    for (OrderItem orderItem : order.getOrderItems()) {
	        OrderItemDTOPretty orderItemDTO = new OrderItemDTOPretty();
	        orderItemDTO.setProductId(orderItem.getProduct().getProductId());
	        orderItemDTO.setProductName(orderItem.getProduct().getProductName());
	        orderItemDTO.setQuantity(orderItem.getQuantity());
	        orderItemDTO.setTotalPrice(orderItem.getTotalPrice());
	        orderItemDTOs.add(orderItemDTO);
	        
	    }
	    orderDTO.setItems(orderItemDTOs);
	       
	   return orderDTO;
		
	}
    public CustomerOrderDTO toDTO(CustomerOrder order) {
    CustomerOrderDTO orderDTO = new CustomerOrderDTO();
    orderDTO.setOrderId(order.getOrderId());
    orderDTO.setOrderDate(order.getOrderDate());
    orderDTO.setBillAmount(order.getBillAmount());
    orderDTO.setTrackingId(order.getTrackingId());
    orderDTO.setOrderStatus(order.getOrderStatus());
    orderDTO.setCustomer(custService.toDTO(order.getCustomer()));
    orderDTO.setExpectedDelivery(order.getExpectedDelivery());
    orderDTO.setDeliveryAddress(order.getDeliveryAddress());
    orderDTO.setFeedback(order.getFeedback());
    
    
    Set<OrderItemDTOPretty> orderItemDTOs = new HashSet<>();
    for (OrderItem orderItem : order.getOrderItems()) {
        OrderItemDTOPretty orderItemDTO = new OrderItemDTOPretty();
        orderItemDTO.setProductId(orderItem.getProduct().getProductId());
        orderItemDTO.setProductName(orderItem.getProduct().getProductName());
        orderItemDTO.setProductPrice(orderItem.getProduct().getProductPrice());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setTotalPrice(orderItem.getTotalPrice());
        orderItemDTOs.add(orderItemDTO);
        
    }
    orderDTO.setItems(orderItemDTOs);
       
   return orderDTO;

}
}
