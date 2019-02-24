package com.vesna1010.onlinebooks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.vesna1010.onlinebooks.exception.ErrorDetails;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler
	public ErrorDetails handleResourceNotFoundException(ResourceNotFoundException e) {
		return new ErrorDetails(e.getMessage());
	}
	
}