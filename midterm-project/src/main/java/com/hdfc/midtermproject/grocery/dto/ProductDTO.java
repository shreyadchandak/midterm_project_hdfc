package com.hdfc.midtermproject.grocery.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Validated
public class ProductDTO {

	  
	  private long productId;

	  @NotNull(message = "producName cannot be null")
	  @NotBlank(message="productName cannot be blank")
	  @Size(min = 1, max = 50, message = "Name length should be between 1 and 50")
	  @Pattern(regexp = "^[a-zA-Z ]+$", message = "productName should contain only alphabets and spaces")
	  private String productName;

	  @NotNull(message = "productDescription cannot be null")
	  @NotBlank(message="productDescription cannot be blank")
	  @Size(min = 1, max = 50, message = "Description length should be between 1 and 50")
	  private String productDescription;

	  @Positive(message="Enter the productPrice greater than zero")
	  private double productPrice;

	  @NotNull(message = "productBrand cannot be null")
	  @NotBlank(message="productBrand cannot be blank")
	  @Size(min = 1, max = 50, message = "Name length should be between 1 and 50")
	  @Pattern(regexp = "^[a-zA-Z ]+$", message = "brandName should contain only alphabets and spaces")
	  private String productBrand;

	  @NotNull(message = "Name cannot be null")
	  @NotBlank(message="Name cannot be blank")
	  @Size(min = 1, max = 50, message = "Name length should be between 1 and 50")
	  @Pattern(regexp = "^[a-zA-Z ]+$", message = "categoryName should contain only alphabets and spaces")
	  private String productCategory;
	  private boolean live;
	  private boolean stock;
	  
	  @NotNull
	  @Pattern(regexp = "^[a-zA-Z0-9-_]+\\.(jpg|jpeg|png|gif)$", message = "Product image name should have a valid file extension")
	  private String productImageName;
	  
	  
}
