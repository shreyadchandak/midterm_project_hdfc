package com.hdfc.midtermproject.grocery.repository;

import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.entity.Review;

@Service
public interface IReviewService {
    public String addReview(Review review) ;

}
