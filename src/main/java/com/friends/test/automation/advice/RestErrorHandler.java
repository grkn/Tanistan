package com.friends.test.automation.advice;

import com.friends.test.automation.controller.resource.ErrorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestErrorHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.error("Validation Exception", exception);
        String bodyOfResponse = exception.getMessage();
        return new ResponseEntity(ErrorResource.ErrorContent.builder().message(bodyOfResponse).build(""), headers,
                status);
    }
}