package com.friends.tanistan.exception;

import com.friends.tanistan.controller.resource.ErrorResource;

public class AlreadyExistsException extends RuntimeException {
    private ErrorResource errorResource;

    public AlreadyExistsException(ErrorResource errorResource) {
        super();
        this.errorResource = errorResource;
    }

    public ErrorResource getErrorResource() {
        return errorResource;
    }

    public void setErrorResource(ErrorResource errorResource) {
        this.errorResource = errorResource;
    }
}
