package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	private final UserRepository userRepository;

	public LoginServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean isValidLoginRequest(final Credentials request) {
		LOGGER.debug("In isValidLoginRequest - Trying to authenticate user with username={}", request.getUserName());
		final var user = getByUserName(request.getUserName());
		if (user.isPresent()) {
			return request.getPassword().equals(user.get().getPassword());
		}
		return false;
	}

	public boolean changePassword(final ChangePasswordRequest request) {
		LOGGER.debug("In changePassword - Trying to change password for user with username={}", request.getUserName());
		final var userOptional = getByUserName(request.getUserName());
		if (userOptional.isPresent() && userOptional.get().getPassword().equals(request.getOldPassword())) {
			final User user = userOptional.get();
			user.setPassword(request.getNewPassword());
			userRepository.saveAndFlush(user);
			return true;
		}
		return false;
	}

	private Optional<User> getByUserName(final String userName) {
		return userRepository.findByUserName(userName);
	}
}
