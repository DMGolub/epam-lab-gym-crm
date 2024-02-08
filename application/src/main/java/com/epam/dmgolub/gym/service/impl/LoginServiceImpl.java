package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public LoginServiceImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public boolean changePassword(final ChangePasswordRequest request) {
		LOGGER.debug("In changePassword - Trying to change password for user with username={}", request.getUserName());
		final var userOptional = getByUserName(request.getUserName());
		if (userOptional.isPresent() && isPasswordValid(request.getOldPassword(), userOptional.get().getPassword())) {
			final var user = userOptional.get();
			user.setPassword(passwordEncoder.encode(request.getNewPassword()));
			userRepository.saveAndFlush(user);
			return true;
		}
		LOGGER.debug("In changePassword - Failed to change password for userName={}", request.getUserName());
		return false;
	}

	private Optional<User> getByUserName(final String userName) {
		return userRepository.findByUserName(userName);
	}

	private boolean isPasswordValid(final String rawPassword, final String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
