package com.epam.dmgolub.gym.service.utility;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;

import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;

public class ServiceUtilities {

	private ServiceUtilities() {
		// Empty
	}

	public static HttpHeaders getAuthHeaders(final HttpSession session) {
		final CredentialsDTO login = (CredentialsDTO) session.getAttribute(LOGIN);
		final var authHeaders = new HttpHeaders();
		authHeaders.set("userName", login.getUserName());
		authHeaders.set("password", login.getPassword());
		return authHeaders;
	}
}
