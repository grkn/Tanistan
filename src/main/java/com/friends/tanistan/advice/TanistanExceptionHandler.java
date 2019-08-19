package com.friends.tanistan.advice;

import com.friends.tanistan.exception.AlreadyExistsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.friends.tanistan.controller.resource.ErrorResource;
import com.friends.tanistan.exception.NotFoundException;

@ControllerAdvice
public class TanistanExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ NotFoundException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleNotFound(Exception ex, WebRequest request) {
		return new ResponseEntity<ErrorResource>(((NotFoundException) ex).getErrorResource(), new HttpHeaders(),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ AlreadyExistsException.class })
	@ResponseBody
	public ResponseEntity<ErrorResource> handleAlreadyExist(Exception ex, WebRequest request) {
		return new ResponseEntity<ErrorResource>(((AlreadyExistsException) ex).getErrorResource(), new HttpHeaders(),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ AccessDeniedException.class })
	@ResponseBody
	public ResponseEntity<Object> handleAuthorization(Exception ex, WebRequest request) {
		return new ResponseEntity<Object>("You don't have permission for this operation", new HttpHeaders(),
				HttpStatus.UNAUTHORIZED);
	}

	// Latest Solution
	@ExceptionHandler({ Throwable.class })
	public ResponseEntity<Object> handleExceptionLatest(Exception ex, WebRequest request) {
		ex.printStackTrace();
		return new ResponseEntity<Object>("Exception occured. Server error", new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
