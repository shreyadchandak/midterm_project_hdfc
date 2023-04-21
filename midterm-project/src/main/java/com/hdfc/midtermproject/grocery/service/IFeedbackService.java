package com.hdfc.midtermproject.grocery.service;

import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;

@Service
public interface IFeedbackService {
public String giveFeedback(FeedbackRequestItem request) throws CustomerNotFound, OrderNotAvailable ;
public String giveRating(ReviewRequestItem request) throws CustomerNotFound, ProductNotFound, OrderNotAvailable ;
public double showRating(Long productId) throws ProductNotFound ;


}
