package com.hdfc.midtermproject.grocery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hdfc.midtermproject.grocery.entity.Product;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	public List<Product> findByProductNameIgnoreCase(String pname);
	public List<Product> findByProductBrandIgnoreCase(String bname);
	public List<Product> findByProductCategoryIgnoreCase(String cname);
}
