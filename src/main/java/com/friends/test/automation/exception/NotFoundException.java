package com.friends.test.automation.exception;

import com.friends.test.automation.controller.resource.ErrorResource;

public class NotFoundException extends RuntimeException {
	private ErrorResource errorResource;

	public NotFoundException(ErrorResource errorResource) {
		this.errorResource = errorResource;
	}

	public ErrorResource getErrorResource() {
		return errorResource;
	}

	public void setErrorResource(ErrorResource errorResource) {
		this.errorResource = errorResource;
	}

}
