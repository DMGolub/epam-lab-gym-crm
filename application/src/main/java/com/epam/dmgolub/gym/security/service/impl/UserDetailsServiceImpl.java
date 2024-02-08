package com.epam.dmgolub.gym.security.service.impl;

import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.security.exception.UserBlockedException;
import com.epam.dmgolub.gym.security.service.BruteForceLoginProtectionService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final BruteForceLoginProtectionService bruteForceLoginProtectionService;
	private final UserRepository userRepository;

	@Value("${authentication.failed.attempts-limit}")
	private int failedAttemptsLimit;
	@Value("${authentication.failed.timeout-minutes}")
	private int authenticationTimeoutMinutes;

	public UserDetailsServiceImpl(
		final BruteForceLoginProtectionService bruteForceLoginProtectionService,
		final UserRepository userRepository
	) {
		this.bruteForceLoginProtectionService = bruteForceLoginProtectionService;
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		if (bruteForceLoginProtectionService.isBlocked(username)) {
			LOGGER.info("In loadUserByUsername - User with userName={} is blocked", username);
			final String message = "User %s has been blocked for %d minutes after %d unsuccessful attempts";
			throw new UserBlockedException(
				String.format(message, username, authenticationTimeoutMinutes, failedAttemptsLimit));
		}

		LOGGER.debug("In loadUserByUsername - Fetching user with userName={} from repository", username);
		final var user = userRepository.findByUserName(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));
		LOGGER.debug("In loadUserByUsername - User with userName={} fetched successfully", username);
		return new User(user.getUserName(), user.getPassword(), Collections.emptyList());
	}
}
