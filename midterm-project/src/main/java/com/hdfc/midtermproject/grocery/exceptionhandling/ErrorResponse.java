package com.hdfc.midtermproject.grocery.exceptionhandling;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ErrorResponse {

	    private String message;
	    private List<FieldError> errors;
	    
	    public ErrorResponse(String message) {
	        this.message = message;
	        this.errors = new ArrayList<>();
	    }

	    public void addFieldError(String field, String message) {
	        FieldError error = new FieldError(field, message);
	        errors.add(error);
	    }
}
