package com.epam.dmgolub.gym.service.exception;

public class EntityNotFoundException extends RuntimeException {

	public EntityNotFoundException(final String message) {
		super(message);
	}
}
