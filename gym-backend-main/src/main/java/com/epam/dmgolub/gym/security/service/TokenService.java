package com.epam.dmgolub.gym.security.service;

public interface TokenService {

	String generateToken(String userName);

	void denyToken(String token);

	String extractUsername(String token);

	boolean isValidToken(String token, String userName);
}
