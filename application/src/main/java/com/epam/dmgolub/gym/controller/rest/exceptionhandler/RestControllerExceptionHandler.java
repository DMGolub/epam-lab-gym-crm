package com.epam.dmgolub.gym.controller.rest.exceptionhandler;

import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.controller.rest.constant.Constants.CONTROLLER_EXCEPTION_LOG_MESSAGE;

@RestControllerAdvice("com.epam.dmgolub.gym.controller.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFoundException(
		final RuntimeException ex,
		final WebRequest request
	) {
		LOG.error(CONTROLLER_EXCEPTION_LOG_MESSAGE, request.getSessionId(), ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@NonNull
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		final MethodArgumentNotValidException ex,
		@NonNull final HttpHeaders headers,
		@NonNull final HttpStatus status,
		@NonNull final WebRequest request
	) {
		final List<String> details = new ArrayList<>();
		for (var error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}
		final String message = "Failed to process request data due to validation errors: " + details;
		LOG.error(CONTROLLER_EXCEPTION_LOG_MESSAGE, request.getSessionId(), message);
		return handleExceptionInternal(ex, message, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
	}

	@NonNull
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		final HttpMessageNotReadableException ex,
		@NonNull final HttpHeaders headers,
		@NonNull final HttpStatus status,
		@NonNull final WebRequest request
	) {
		final String cause = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
		final String message = "Failed to process request data due to: " +  cause;
		return handleExceptionInternal(ex, message, headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<Object> exception(final RuntimeException ex, final WebRequest request) {
		LOG.error(CONTROLLER_EXCEPTION_LOG_MESSAGE, request.getSessionId(), ex.getMessage());
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
