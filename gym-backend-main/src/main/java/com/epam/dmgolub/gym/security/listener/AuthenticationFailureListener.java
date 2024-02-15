package com.epam.dmgolub.gym.security.listener;

import com.epam.dmgolub.gym.security.service.BruteForceLoginProtectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFailureListener.class);

	private final BruteForceLoginProtectionService bruteForceLoginProtectionService;

	public AuthenticationFailureListener(final BruteForceLoginProtectionService bruteForceLoginProtectionService) {
		this.bruteForceLoginProtectionService = bruteForceLoginProtectionService;
	}

	@Override
	public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent event) {
		final String userName = event.getAuthentication().getName();
		LOGGER.info("In onApplicationEvent - Received authentication failure event for user={}", userName);
		bruteForceLoginProtectionService.updateFailedAttempts(userName);
	}
}
