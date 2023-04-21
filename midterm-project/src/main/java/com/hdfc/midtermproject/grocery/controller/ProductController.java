package com.hdfc.midtermproject.grocery.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdfc.midtermproject.grocery.dto.ProductDTO;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.service.IProductService;

@RestController
@RequestMapping("/product")
@Validated
public class ProductController {

	@Autowired
	IProductService service;
	
	@PostMapping("/admin/insertProduct")
	public ProductDTO createProduct(@Valid @RequestBody ProductDTO productdto)
	{
		return service.insertProduct(productdto) ;
	}
	
	@PutMapping("/admin/updateProduct")
	public ProductDTO updateProduct(@Valid @RequestBody ProductDTO productdto)
	{
		return service.updateProduct(productdto);
	}
	@GetMapping("/admin/getProductById/{productId}")
	public ProductDTO getById(@PathVariable @Positive(message="Plese enter productId greater than zero")long productId) throws ProductNotFound {
		return service.getById(productId);
	}
	@DeleteMapping("/admin/deleteProductById/{productId}")
	public String deleteProduct(@PathVariable @Positive(message="Please enter productId greater than zero")long productId) throws ProductNotFound
	{
		return service.deleteProduct(productId);
		
	}
	@GetMapping("/user/getByProductName/{productName}")
	public List<ProductDTO> getByName(@PathVariable @NotNull(message="Enter the productName") @NotBlank(message="Enter the productName") String productName) throws ProductNotFound{
		
		return service.findByProductName(productName);
	}
	
	@GetMapping("/user/getByproductBrand/{brandName}")
    public List<ProductDTO> getByBrand(@PathVariable @NotNull(message="Enter the BrandName") @NotBlank(message="Enter the BrandName") String brandName) throws ProductNotFound{
		
		return service.findByProductBrand(brandName);
	}
	
	@GetMapping("/public/getByProductCategory/{categoryName}")
    public List<ProductDTO> getByCategory(@PathVariable @NotNull(message="Enter the categoryName") @NotBlank(message="Enter the categoryName")String categoryName) throws ProductNotFound{
		
		return service.findByProductCategory(categoryName);
	}
	
	@GetMapping("/user/getAll")
	public List<ProductDTO> getAll() throws ProductNotFound{
		return service.findAll();
	}
}
