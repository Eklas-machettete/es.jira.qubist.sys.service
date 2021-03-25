package com.jira.exception;

import java.lang.NullPointerException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionAdvice {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionAdvice.class);
	@ExceptionHandler(StringIndexOutOfBoundsException.class)
    public ResponseEntity<CustomErrorResponse> stringOutBounds(StringIndexOutOfBoundsException ex){
		 LOGGER.error("Este es el error：" + ex.getMessage(),ex);
        CustomErrorResponse error = new CustomErrorResponse("ERROR_02", ex.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus((HttpStatus.NOT_FOUND.value()));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomErrorResponse> nullPointerException(NullPointerException ex){
		 LOGGER.error("Este es el error：" + ex.getMessage(),ex);
        CustomErrorResponse error = new CustomErrorResponse("ERROR_01", ex.getMessage());
		error.setTimestamp(LocalDateTime.now());
		error.setStatus((HttpStatus.NOT_FOUND.value()));
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}