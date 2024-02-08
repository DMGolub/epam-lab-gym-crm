package com.epam.dmgolub.gym.security.utility;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class SecurityUtils {

	private static final String BEARER = "Bearer ";
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

	public static Optional<String> extractBearerToken(final HttpServletRequest request) {

		final String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
		if (authHeader != null && authHeader.startsWith(BEARER)) {
			return Optional.of(authHeader.substring(BEARER.length()));
		}
		return Optional.empty();
	}

	private SecurityUtils() {
		// Empty
	}
}
