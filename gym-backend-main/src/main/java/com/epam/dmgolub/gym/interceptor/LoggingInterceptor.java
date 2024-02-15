package com.epam.dmgolub.gym.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

	@Override
	public boolean preHandle(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler
	) {
		LOGGER.debug("In preHandle - Received a request: {} {}", request.getMethod(), request.getRequestURL());
		final String transactionId = UUID.randomUUID().toString();
		MDC.put("transactionId", transactionId);
		return true;
	}

	@Override
	public void afterCompletion(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler,
		final Exception ex
	) {
		LOGGER.debug("In afterCompletion - Completed request: {} {}, status: {}",
			request.getMethod(), request.getRequestURL(), response.getStatus());
	}
}
