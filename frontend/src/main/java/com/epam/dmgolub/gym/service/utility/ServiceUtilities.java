package com.epam.dmgolub.gym.service.utility;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;

public class ServiceUtilities {

	private static final String JWT_TOKEN_ATTRIBUTE_NAME = "jwtToken";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";

	private ServiceUtilities() {
		// Empty
	}

	public static HttpHeaders getAuthHeader(final HttpSession session) {
		final String token = (String) session.getAttribute(JWT_TOKEN_ATTRIBUTE_NAME);
		final var authHeader = new HttpHeaders();
		authHeader.set(AUTHORIZATION_HEADER_NAME, BEARER_PREFIX + token);
		return authHeader;
	}
}
