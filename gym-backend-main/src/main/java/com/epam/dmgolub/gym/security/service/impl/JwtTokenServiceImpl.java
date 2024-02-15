package com.epam.dmgolub.gym.security.service.impl;

import com.epam.dmgolub.gym.security.service.TokenService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class JwtTokenServiceImpl implements TokenService {

	private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenServiceImpl.class);

	private final long tokenExpirationTimeMillis;
	private final LoadingCache<String, Boolean> deniedTokens;
	@Value("${authentication.jwt.secret-key}")
	private String secretKey;

	public JwtTokenServiceImpl(@Value("${authentication.jwt.token.expiration-time}") final long tokenExpirationTimeMillis) {
		this.tokenExpirationTimeMillis = tokenExpirationTimeMillis;
		deniedTokens = CacheBuilder.newBuilder().expireAfterWrite(tokenExpirationTimeMillis, MILLISECONDS).build(
			new CacheLoader<>() {
				@NonNull
				@Override
				public Boolean load(@NonNull final String token) {
					return false;
				}
			}
		);
	}

	@Override
	public String generateToken(final String userName) {
		LOGGER.debug("In generateToken - generating token for userName={}", userName);
		final Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(userName)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTimeMillis))
			.signWith(signatureAlgorithm, secretKey)
			.compact();
	}

	@Override
	public void denyToken(final String token) {
		deniedTokens.put(token, true);
		LOGGER.debug("In denyToken - Added token {} to list", token);
	}

	@Override
	public String extractUsername(final String token) {
		return extractAllClaims(token).getSubject();
	}

	@Override
	public boolean isValidToken(final String token, final String userName) {
		final String tokenUserName = extractUsername(token);
		return userName.equals(tokenUserName) && !isExpired(token) && !isDenied(token);
	}

	private boolean isExpired(final String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	private boolean isDenied(final String token) {
		try {
			return deniedTokens.get(token);
		} catch (final ExecutionException e) {
			return false;
		}
	}

	private Claims extractAllClaims(final String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
}
