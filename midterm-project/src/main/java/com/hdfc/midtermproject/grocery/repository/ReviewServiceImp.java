package com.hdfc.midtermproject.grocery.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdfc.midtermproject.grocery.entity.Review;
import com.hdfc.midtermproject.grocery.service.ReviewRepo;
@Service
public class ReviewServiceImp {


	    
	    @Autowired
	    private ReviewRepo reviewRepository;
	    
	    public String addReview(Review review) {
	        reviewRepository.save(review);
	        updateProductRating(review.getProductId());
	        return "Your rating is saved as "+review.getRating()+" !Thanks for taking time to rate the product";
	    }
	    
	    private void updateProductRating(Long productId) {
	        List<Review> reviews = reviewRepository.findByProductId(productId);
	        if (reviews.isEmpty()) {
	            // no reviews, set product rating to 0
	            setProductRating(productId, 0);
	        } else {
	            // calculate average rating
	            double averageRating = reviews.stream()
	                    .mapToDouble(Review::getRating)
	                    .average()
	                    .orElse(0);
	            // update product rating
	            setProductRating(productId, averageRating);
	        }
	    }

	    @Transactional
	    private void setProductRating(Long productId, double rating) {
	        reviewRepository.updateReviewByProductId(productId, rating);

	    }
	}



