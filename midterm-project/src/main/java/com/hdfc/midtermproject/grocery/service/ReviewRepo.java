package com.hdfc.midtermproject.grocery.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hdfc.midtermproject.grocery.entity.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

 
        @Transactional
        @Modifying
        @Query("UPDATE Review r SET r.rating = :rating WHERE r.productId = :productId")
        void updateReviewByProductId(@Param("productId") Long productId, @Param("rating") double rating);
    

}
