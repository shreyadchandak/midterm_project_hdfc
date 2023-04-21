package com.hdfc.midtermproject.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hdfc.midtermproject.grocery.entity.CustomerOrder;

@Repository

public interface OrderRepo extends JpaRepository<CustomerOrder,Long>{

  public CustomerOrder findByTrackingId(String trackingId);
}
