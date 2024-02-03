package com.epam.dmgolub.gym.service.utility;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;

import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;

public class ServiceUtilities {

	private ServiceUtilities() {
		// Empty
	}

	public static HttpHeaders getAuthHeaders(final HttpSession session) {
		final CredentialsDTO login = (CredentialsDTO) session.getAttribute(LOGIN);
		final var authHeaders = new HttpHeaders();
		final String auth = login.getUserName() + ":" + login.getPassword();
		final String authHeader = "Basic " + new String(Base64.encodeBase64(auth.getBytes(), false));
		authHeaders.set( "Authorization", authHeader );
		return authHeaders;
	}
}
