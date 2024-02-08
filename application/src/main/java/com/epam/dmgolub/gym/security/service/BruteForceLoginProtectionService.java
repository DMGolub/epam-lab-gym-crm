package com.epam.dmgolub.gym.security.service;

public interface BruteForceLoginProtectionService {

	void updateFailedAttempts(String userName);

	void resetFailedAttempts(String userName);

	boolean isBlocked(String userName);
}
