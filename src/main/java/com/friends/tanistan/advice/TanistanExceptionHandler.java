package com.friends.tanistan.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.friends.tanistan.controller.resource.ErrorResource;
import com.friends.tanistan.exception.AlreadyExistsException;
import com.friends.tanistan.exception.DriverException;
import com.friends.tanistan.exception.NotFoundException;

@ControllerAdvice
public class TanistanExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(TanistanExceptionHandler.class);

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

	@ExceptionHandler({ InsufficientAuthenticationException.class })
	public ResponseEntity<Object> donthandleException(Exception ex, WebRequest request) throws Exception {
		logger.error("Authenticate error :", ex);
		return new ResponseEntity<Object>("Permission Denied by Authentication mechanism", new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ DriverException.class })
	public ResponseEntity<Object> handleDriverException(Exception ex, WebRequest request) throws Exception {
		logger.error("Driver error :", ex);
		return new ResponseEntity<Object>(((AlreadyExistsException) ex).getErrorResource(), new HttpHeaders(),
				HttpStatus.BAD_REQUEST);
	}

	// Latest Solution
	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<Object> handleExceptionLatest(Exception ex, WebRequest request) {
		logger.error("Unknown error :", ex);
		return new ResponseEntity<Object>("Exception occured. Server error", new HttpHeaders(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
