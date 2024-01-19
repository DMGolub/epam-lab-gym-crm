package com.epam.dmgolub.gym.controller.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PasswordChangeFailedException extends RuntimeException {

	public PasswordChangeFailedException(final String message) {
		super(message);
	}
}
