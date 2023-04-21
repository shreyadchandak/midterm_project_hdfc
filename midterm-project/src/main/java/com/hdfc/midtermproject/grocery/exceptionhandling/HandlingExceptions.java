package com.hdfc.midtermproject.grocery.exceptionhandling;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hdfc.midtermproject.grocery.exception.CartIsEmpty;
import com.hdfc.midtermproject.grocery.exception.ChoiceNotValid;
import com.hdfc.midtermproject.grocery.exception.CustomerAlreadyExists;
import com.hdfc.midtermproject.grocery.exception.CustomerNotFound;
import com.hdfc.midtermproject.grocery.exception.ItemNotFound;
import com.hdfc.midtermproject.grocery.exception.MaximumQuantityForProductExceeded;
import com.hdfc.midtermproject.grocery.exception.NoOrdersAvailable;
import com.hdfc.midtermproject.grocery.exception.OrderNotAvailable;
import com.hdfc.midtermproject.grocery.exception.PaymentsNotFound;
import com.hdfc.midtermproject.grocery.exception.ProductNotAvailable;
import com.hdfc.midtermproject.grocery.exception.ProductNotFound;
import com.hdfc.midtermproject.grocery.exception.ReviewNotGiven;


@RestControllerAdvice
public class HandlingExceptions {

	@ExceptionHandler(ProductNotFound.class)
	public ResponseEntity<String> handleExp(ProductNotFound product){
		return new ResponseEntity<String>(product.getMessage(),HttpStatus.BAD_REQUEST);
}
	
	@ExceptionHandler(ItemNotFound.class)
	public ResponseEntity<String> handleExp(ItemNotFound item){
		return new ResponseEntity<String>(item.getMessage(),HttpStatus.BAD_REQUEST);
}
	@ExceptionHandler(CustomerNotFound.class)
	public ResponseEntity<String> handleExp(CustomerNotFound customer){
		return new ResponseEntity<String>(customer.getMessage(),HttpStatus.BAD_REQUEST);
}
	@ExceptionHandler(CartIsEmpty.class)
	public ResponseEntity<String> handleExp(CartIsEmpty cart){
		return new ResponseEntity<String>(cart.getMessage(),HttpStatus.BAD_REQUEST);
}

   @ExceptionHandler(NoOrdersAvailable.class)
   public ResponseEntity<String> handleExp(NoOrdersAvailable orders){
	return new ResponseEntity<String>(orders.getMessage(),HttpStatus.BAD_REQUEST);
}
   @ExceptionHandler(OrderNotAvailable.class)
   public ResponseEntity<String> handleExp(OrderNotAvailable order){
   return new ResponseEntity<String>(order.getMessage(),HttpStatus.BAD_REQUEST);

}
   @ExceptionHandler(ChoiceNotValid.class)
   public ResponseEntity<String> handleExp(ChoiceNotValid choice){
   return new ResponseEntity<String>(choice.getMessage(),HttpStatus.BAD_REQUEST);
   }
   
   @ExceptionHandler(PaymentsNotFound.class)
   public ResponseEntity<String> handleExp(PaymentsNotFound payment){
	   return new ResponseEntity<String>(payment.getMessage(),HttpStatus.BAD_REQUEST);
   }
   @ExceptionHandler(ProductNotAvailable.class)
   public ResponseEntity<String> handleExp(ProductNotAvailable product){
	   return new ResponseEntity<String>(product.getMessage(),HttpStatus.BAD_REQUEST);
   }
   @ExceptionHandler(CustomerAlreadyExists.class)
   public ResponseEntity<String> handleExp(CustomerAlreadyExists customer){
	   return new ResponseEntity<String>(customer.getMessage(),HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(ReviewNotGiven.class)
   public ResponseEntity<String> handleExp(ReviewNotGiven review){
	   return new ResponseEntity<String>(review.getMessage(),HttpStatus.BAD_REQUEST);
   }
@ExceptionHandler(MaximumQuantityForProductExceeded.class)
public ResponseEntity<String> handleExp(MaximumQuantityForProductExceeded product){
	   return new ResponseEntity<String>(product.getMessage(),HttpStatus.BAD_REQUEST);
}
   
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<List<String>> getErrors(ConstraintViolationException ex){
		List<String> errors=new ArrayList<>();
		Set<ConstraintViolation<?>> violations=ex.getConstraintViolations();
		for(ConstraintViolation<?> violation:violations) {
			errors.add(violation.getPropertyPath()+" : "+violation.getMessage());
		}
		return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
		
	}
	

	    @ExceptionHandler(MethodArgumentNotValidException.class)
	    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
	        BindingResult bindingResult = ex.getBindingResult();
	        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

	        ErrorResponse errorResponse = new ErrorResponse("Validation failed");
	        for (FieldError fieldError : fieldErrors) {
	            errorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
	        }

	        return ResponseEntity.badRequest().body(errorResponse);
	    }
	}



