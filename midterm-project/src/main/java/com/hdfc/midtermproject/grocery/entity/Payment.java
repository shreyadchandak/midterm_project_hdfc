package com.hdfc.midtermproject.grocery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long paymentId;
	private String paymentMethod="def";
	private String paymentStatus="PENDING";
	private double paymentAmount;
	private long orderId;
	private String email;
	
	/*@OneToOne
	@JoinColumn(name="order_id")
	private CustomerOrder order; */
	
}
