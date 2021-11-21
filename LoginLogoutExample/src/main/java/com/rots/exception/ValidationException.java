package com.rots.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus exceptionCode;
	private String exceptionMessage;
	
	public ValidationException(String message, HttpStatus code){
		this.exceptionCode = code;
		this.exceptionMessage = message;
	}

	public HttpStatus getExceptionCode() {
		return exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}
	
	
}
