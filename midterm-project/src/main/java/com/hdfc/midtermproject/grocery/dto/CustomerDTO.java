package com.hdfc.midtermproject.grocery.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Validated
public class CustomerDTO {

	private long customerId;
	
	@NotNull(message = "Name cannot be null")
	@NotBlank(message="Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name length should be between 1 and 50")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name should contain only alphabets and spaces")
 	private String customerName;
	@NotNull
	@Email(message="Enter the correct emailId")
 	private String customerEmail;
	@NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password length should be between 8 and 20")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$", message = "Password should contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
 	private String customerPassword="-";
	@NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+91[6-9]\\d{9}$", message = "Phone number should start with +91[6-9] and be followed by nine digits")
 	private String customerPhone;
	@NotNull
	@NotBlank
 	private String customerAddress;
	
 	private boolean active;
}
