package com.demo.concurrency.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<Object> handleClientNotFoundException(ClientNotFoundException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("message", "Client not found");
		
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NotEnoughBalanceException.class)
	public ResponseEntity<Object> handleNotEnoughBalanceException(NotEnoughBalanceException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", Instant.now());
		body.put("message", "Not enough money to do the operation.");
		
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
}
