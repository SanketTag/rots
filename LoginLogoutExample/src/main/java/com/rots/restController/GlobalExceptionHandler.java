package com.rots.restController;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rots.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> handleValidationException(HttpServletRequest request, ValidationException ex){
		logger.info("Handling Exception Message - " + ex.getExceptionMessage());
		return new ResponseEntity<String>(ex.getExceptionMessage(),ex.getExceptionCode());
	}
}
