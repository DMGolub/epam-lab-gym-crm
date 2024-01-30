package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.interceptor.SessionLoginInterceptor;
import com.epam.dmgolub.gym.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	private final RestTemplate restTemplate;

	@Value("${backend.url}")
	private String backendUrl;

	public LoginServiceImpl(final RestTemplate restTemplate, final SessionLoginInterceptor interceptor) {
		this.restTemplate = restTemplate;
		interceptor.setLoginService(this);
	}

	@Override
	public boolean isValidLoginRequest(final CredentialsDTO credentialsDTO) {
		LOGGER.debug("In isValidLoginRequest - Received a request to authenticate user={}", credentialsDTO.getUserName());
		final String requestUrl = backendUrl + "/api/v1/login";
		try {
			restTemplate.postForLocation(requestUrl, credentialsDTO);
			return true;
		} catch (final HttpClientErrorException ex) {
			LOGGER.debug("In isValidLoginRequest - Received an exception: {}", ex.getMessage());
			return false;
		}
	}

	@Override
	public boolean changePassword(final ChangePasswordRequestDTO requestDTO) {
		LOGGER.debug("In changePassword - Received a request to change password for user={}", requestDTO.getUserName());
		final String requestUrl = backendUrl + "/api/v1/login";
		try {
			restTemplate.put(requestUrl, requestDTO);
			return true;
		} catch (final HttpClientErrorException ex) {
			LOGGER.debug("In changePassword - Received an exception: {}", ex.getMessage());
			return false;
		}
	}
}
