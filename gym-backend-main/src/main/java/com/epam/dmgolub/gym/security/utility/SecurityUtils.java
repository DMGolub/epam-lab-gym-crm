package com.epam.dmgolub.gym.security.utility;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class SecurityUtils {

	private static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

	public static Optional<String> extractBearerToken(final HttpServletRequest request) {
		final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
		if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
			return Optional.of(authHeader.substring(BEARER_PREFIX.length()));
		}
		return Optional.empty();
	}

	private SecurityUtils() {
		// Empty
	}
}
