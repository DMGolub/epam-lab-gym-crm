package com.epam.dmgolub.gym.interceptor;

import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.service.LoginService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeaderLoginInterceptor implements HandlerInterceptor {

	private static final String USERNAME_HEADER = "userName";
	private static final String PASSWORD_HEADER = "password";
	private static final Logger LOGGER = LoggerFactory.getLogger(HeaderLoginInterceptor.class);

	private final LoginService loginService;
	private final Counter authenticationSuccessCounter;
	private final Counter authenticationFailCounter;

	public HeaderLoginInterceptor(final LoginService loginService, final MeterRegistry meterRegistry) {
		this.loginService = loginService;
		authenticationSuccessCounter = Counter.builder("rest_authentication_success_counter")
			.description("Number of successful authentications")
			.register(meterRegistry);
		authenticationFailCounter = Counter.builder("rest_authentication_fail_counter")
			.description("Number of failed authentications")
			.register(meterRegistry);
	}

	@Override
	public boolean preHandle(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler
	) {
		final String userName = request.getHeader(USERNAME_HEADER);
		final String password = request.getHeader(PASSWORD_HEADER);
		if (isValidLogin(userName, password)) {
			LOGGER.debug("In preHandle - Successfully authenticated user={}", userName);
			authenticationSuccessCounter.increment();
			return true;
		} else {
			LOGGER.debug("In preHandle - Failed to authenticate user={}", userName);
			authenticationFailCounter.increment();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
	}

	private boolean isValidLogin(final String userName, final String password) {
		return userName != null && password != null &&
			loginService.isValidLoginRequest(new Credentials(userName, password));
	}
}
