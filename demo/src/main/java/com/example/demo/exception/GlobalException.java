package com.example.demo.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice	
public class GlobalException extends RuntimeException {
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> userNotFoundException(UsernameNotFoundException usernameNotFoundException) {
		Map<String, String> payload = Map.of("Message", "Invalid user");
		return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> global(Exception exception) {
		Map<String, String> payload = Map.of("Message", exception.getMessage());
		return new ResponseEntity<>(payload, HttpStatus.BAD_REQUEST);
	}

}
