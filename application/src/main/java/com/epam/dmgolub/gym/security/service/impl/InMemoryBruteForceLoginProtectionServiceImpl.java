package com.epam.dmgolub.gym.security.service.impl;

import com.epam.dmgolub.gym.security.service.BruteForceLoginProtectionService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Service
public class InMemoryBruteForceLoginProtectionServiceImpl implements BruteForceLoginProtectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBruteForceLoginProtectionServiceImpl.class);

	private final LoadingCache<String, Integer> failedAttempts;
	private final LoadingCache<String, Boolean> blockedUsers;

	private final int authenticationTimeoutMinutes;
	@Value("${authentication.failed.attempts-limit}")
	private int failedAttemptsLimit;

	public InMemoryBruteForceLoginProtectionServiceImpl(
		@Value("${authentication.failed.timeout-minutes:5}") final int authenticationTimeoutMinutes
	) {
		this.authenticationTimeoutMinutes = authenticationTimeoutMinutes;
		failedAttempts = CacheBuilder.newBuilder().expireAfterWrite(1, DAYS).build(
			new CacheLoader<>() {
				@NonNull
				@Override
				public Integer load(@NonNull final String key) {
					return 0;
				}
			}
		);
		blockedUsers = CacheBuilder.newBuilder().expireAfterWrite(authenticationTimeoutMinutes, MINUTES).build(
			new CacheLoader<>() {
				@NonNull
				@Override
				public Boolean load(@NonNull final String key) {
					return false;
				}
			}
		);
	}

	@Override
	public void updateFailedAttempts(final String userName) {
		int attempts;
		try {
			attempts = failedAttempts.get(userName);
		} catch (final ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		if (attempts >= failedAttemptsLimit) {
			blockUser(userName);
			resetFailedAttempts(userName);
		} else {
			failedAttempts.put(userName, attempts);
		}
	}

	@Override
	public void resetFailedAttempts(final String userName) {
		failedAttempts.put(userName, 0);
	}

	@Override
	public boolean isBlocked(final String userName) {
		try {
			return blockedUsers.get(userName);
		} catch (final ExecutionException e) {
			return false;
		}
	}

	private void blockUser(final String userName) {
		blockedUsers.put(userName, true);
		LOGGER.info("In blockUser - Blocked user with userName={} for {} minutes", userName,
			authenticationTimeoutMinutes
		);
	}
}
