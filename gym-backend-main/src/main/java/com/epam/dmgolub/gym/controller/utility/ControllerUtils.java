package com.epam.dmgolub.gym.controller.utility;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.epam.dmgolub.gym.controller.constant.Constants.ACCESS_DENIED_MESSAGE;

public class ControllerUtils {

	private ControllerUtils() {
		// Empty
	}

	public static void checkIsAuthorizedUser(final String userName) {
		final var userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!userDetails.getUsername().equals(userName)) {
			throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
		}
	}
}
