package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.epam.dmgolub.gym.service.constant.Constants.API;
import static com.epam.dmgolub.gym.service.constant.Constants.CHANGE_PASSWORD_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.LOGIN_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.VERSION_V1;

@Service
public class LoginServiceImpl implements LoginService {

	private static final String BASE_URL = API + VERSION_V1;
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	private final RestTemplate restTemplate;

	@Value("${backend.url}")
	private String backendUrl;

	public LoginServiceImpl(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public String logIn(final CredentialsDTO credentialsDTO) {
		LOGGER.debug("In isValidLoginRequest - Received a request to authenticate user={}", credentialsDTO.getUserName());
		final String requestUrl = backendUrl + BASE_URL + LOGIN_LOCATION;
		final String token = restTemplate.postForObject(requestUrl, credentialsDTO, String.class);
		LOGGER.debug("In isValidLoginRequest - User={} authenticated successfully", credentialsDTO.getUserName());
		return token;
	}

	@Override
	public boolean changePassword(final ChangePasswordRequestDTO requestDTO) {
		LOGGER.debug("In changePassword - Received a request to change password for user={}", requestDTO.getUserName());
		final String requestUrl = backendUrl + BASE_URL + CHANGE_PASSWORD_LOCATION;
		try {
			restTemplate.put(requestUrl, requestDTO);
			return true;
		} catch (final HttpClientErrorException ex) {
			LOGGER.debug("In changePassword - Received an exception: {}", ex.getMessage());
			return false;
		}
	}
}
