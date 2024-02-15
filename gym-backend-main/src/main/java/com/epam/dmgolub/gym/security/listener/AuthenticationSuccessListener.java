package com.epam.dmgolub.gym.security.listener;

import com.epam.dmgolub.gym.security.service.BruteForceLoginProtectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessListener.class);

	private final BruteForceLoginProtectionService bruteForceLoginProtectionService;

	public AuthenticationSuccessListener(final BruteForceLoginProtectionService bruteForceLoginProtectionService) {
		this.bruteForceLoginProtectionService = bruteForceLoginProtectionService;
	}

	@Override
	public void onApplicationEvent(final AuthenticationSuccessEvent event) {
		final String userName = event.getAuthentication().getName();
		bruteForceLoginProtectionService.resetFailedAttempts(userName);
		LOGGER.debug("In onApplicationEvent - Login successful, reset number of failed attempts for user={}", userName);
	}
}
