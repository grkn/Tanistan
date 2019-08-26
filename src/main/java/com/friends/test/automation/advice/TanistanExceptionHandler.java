package com.friends.test.automation.advice;

import com.friends.test.automation.controller.resource.ErrorResource;
import com.friends.test.automation.exception.AlreadyExistsException;
import com.friends.test.automation.exception.BadRequestException;
import com.friends.test.automation.exception.DriverException;
import com.friends.test.automation.exception.NotFoundException;
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

@ControllerAdvice
public class TanistanExceptionHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(TanistanExceptionHandler.class);

    @ExceptionHandler({NotFoundException.class})
    @ResponseBody
    public ResponseEntity<ErrorResource> handleNotFound(Exception ex, WebRequest request) {
        return new ResponseEntity<ErrorResource>(((NotFoundException) ex).getErrorResource(), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseBody
    public ResponseEntity<ErrorResource> handleAlreadyExist(Exception ex, WebRequest request) {
        return new ResponseEntity<>(((AlreadyExistsException) ex).getErrorResource(), new HttpHeaders(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseBody
    public ResponseEntity<ErrorResource> handleAuthorization(Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ErrorResource.ErrorContent.builder().message("You don't have permission for this operation").build(""),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InsufficientAuthenticationException.class})
    public ResponseEntity<ErrorResource> donthandleException(Exception ex, WebRequest request) throws Exception {
        logger.error("Authenticate error :", ex);
        return new ResponseEntity<>(ErrorResource.ErrorContent.builder().message("Permission Denied").build(""),
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({DriverException.class})
    public ResponseEntity<ErrorResource> handleDriverException(Exception ex, WebRequest request) throws Exception {
        logger.error("Driver error :", ex);
        return new ResponseEntity<>(((AlreadyExistsException) ex).getErrorResource(), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResource> handleBadRequestException(Exception ex, WebRequest request) throws Exception {
        logger.error("BadRequest error :", ex);
        return new ResponseEntity<>(((BadRequestException) ex).getErrorResource(), new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    // Latest Solution
    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ErrorResource> handleExceptionLatest(Exception ex, WebRequest request) {
        logger.error("Unknown error :", ex);
        return new ResponseEntity<>(
                ErrorResource.ErrorContent.builder().message("Unexpected Server Error").build(""), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
