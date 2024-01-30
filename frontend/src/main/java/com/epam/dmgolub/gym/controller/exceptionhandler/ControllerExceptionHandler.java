package com.epam.dmgolub.gym.controller.exceptionhandler;

import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.epam.dmgolub.gym.controller.constant.Constants.CONTROLLER_EXCEPTION_LOG_MESSAGE;
import static com.epam.dmgolub.gym.controller.constant.Constants.ENTITY_NOT_FOUND_VIEW;
import static com.epam.dmgolub.gym.controller.constant.Constants.EXCEPTION_VIEW;

@ControllerAdvice("com.epam.dmgolub.gym.controller.mvc")
public class ControllerExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String handleEntityNotFoundException(final HttpServletRequest request, final Exception e) {
		LOGGER.error(CONTROLLER_EXCEPTION_LOG_MESSAGE, request.getRequestURL(), e.getMessage());
		return ENTITY_NOT_FOUND_VIEW;
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public String exception(final HttpServletRequest request, final Exception e) {
		LOGGER.error(CONTROLLER_EXCEPTION_LOG_MESSAGE, request.getRequestURL(), e);
		return EXCEPTION_VIEW;
	}
}
