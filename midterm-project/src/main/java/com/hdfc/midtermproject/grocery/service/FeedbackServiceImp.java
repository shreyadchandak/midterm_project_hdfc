package com.hdfc.midtermproject.grocery.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.entity.Customer;
import com.hdfc.midtermproject.grocery.entity.CustomerOrder;
import com.hdfc.midtermproject.grocery.entity.OrderItem;
import com.hdfc.midtermproject.grocery.entity.Review;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.repository.CustomerRepo;
import com.hdfc.midtermproject.grocery.repository.OrderRepo;
import com.hdfc.midtermproject.grocery.repository.ProductRepo;
import com.hdfc.midtermproject.grocery.repository.ReviewServiceImp;

@Service
public class FeedbackServiceImp implements IFeedbackService{

	@Autowired
	OrderRepo ordRepo;
	@Autowired 
	CustomerRepo custRepo;
	@Autowired
	ReviewServiceImp reviewService;
	@Autowired
	ProductRepo proRepo;
	@Autowired
	ReviewRepo reviewRepo;
	
	public String giveFeedback(FeedbackRequestItem request) throws CustomerNotFound, OrderNotAvailable {
		String cust=request.getEmail();
		Customer customer=custRepo.findByCustomerEmail(cust);
		if(customer==null) {
			throw new CustomerNotFound();
		}
		List<CustomerOrder> orders=customer.getOrders();
		boolean flag=false;
		for(CustomerOrder order:orders) {
			if(order.getOrderId()==request.getOrderId())
			{
				flag=true;
				if(order.getFeedback().equals("ENABLE")) {
				String feedback="Stars :"+request.getRATE_OUT_OF_FIVE()+", Positives :"+request.getPOSITIVE_ASPECTS()+", Negatives :"+request.getNEGATIVE_ASPECTS();
				order.setFeedback(feedback);
				ordRepo.save(order);
				return "Saved Feedback as "+order.getFeedback();
				}else
				if(order.getFeedback().equals("DISABLE"))
				{
					return "Cannot give the feedback yet";
				}else
					return "Feedback already given,Cannot edit!";
			}
		}
		if(flag==false) {
			throw new OrderNotAvailable();
		}
		return "";
	}
	public String giveRating(ReviewRequestItem request) throws CustomerNotFound, ProductNotFound, OrderNotAvailable {
	
		if(custRepo.findByCustomerEmail(request.getCustomerEmail())==null) {
			throw new CustomerNotFound();
		}
		Customer customer=custRepo.findByCustomerEmail(request.getCustomerEmail());
		
		for(CustomerOrder order:customer.getOrders()) {
			if(order.getOrderId()==request.getOrderId()) {
			
				for(OrderItem item:order.getOrderItems()) {
					if(item.getProduct().getProductId()==request.getProductId() && order.getOrderStatus().equals("DELIVERED")) {
						if(!item.getRating().equals("-")) {
						   return "Rating already given";
						}
						item.setRating("Given");
						Review review=new Review();
						review.setProductId(request.getProductId());
						review.setRating(request.getRating());
						return reviewService.addReview(review);
					}
				
				}
				throw new ProductNotFound();
			}
		} throw new OrderNotAvailable();
	
	}

   public double showRating(Long productId) throws ProductNotFound {
	   if(proRepo.getById(productId)==null) {
		   throw new ProductNotFound();
	   }
	   Review review= reviewRepo.findById(productId).orElse(null);
	   if(review==null) {
		   return 0.0;
	   }
	   return review.getRating();
	   
   }
}


