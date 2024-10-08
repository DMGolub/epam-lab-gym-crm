package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@Service
public class UserCredentialsGeneratorImpl implements UserCredentialsGenerator {

	private static final String POSSIBLE_PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCredentialsGeneratorImpl.class);

	@Value("${password.generated.length:10}")
	private int passwordLength;

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserCredentialsGeneratorImpl(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String generateUserName(final User user) {
		LOGGER.debug("[{}] In generateUserName - Generating user name for {}", MDC.get(TRANSACTION_ID), user);
		final String userNameRegEx = getUserNameRegEx(user);
		final var similarUserNames = findSimilarUserNames(userNameRegEx);
		final Optional<Long> suffixMaxValue = calculateUserNameSuffixMaxValue(userNameRegEx, similarUserNames);
		final String userName = user.getFirstName() + "." + user.getLastName();
		return suffixMaxValue.map(value -> userName + (value + 1)).orElse(userName);
	}

	@Override
	public String generatePassword(final User user) {
		LOGGER.debug("[{}] In generatePassword - Generating password for {}", MDC.get(TRANSACTION_ID), user);
		char[] chars = POSSIBLE_PASSWORD_CHARACTERS.toCharArray();
		return RandomStringUtils.random(passwordLength, 0, chars.length - 1, true, true, chars, new SecureRandom());
	}

	@Override
	public String encodePassword(final String password) {
		LOGGER.debug("In encodePassword - Encoding provided password");
		return passwordEncoder.encode(password);
	}

	private List<String> findSimilarUserNames(final String userNameRegEx) {
		final List<User> users = new ArrayList<>(userRepository.findAll());
		return users.stream()
			.map(User::getUserName)
			.filter(userName -> Pattern.matches(userNameRegEx, userName))
			.toList();
	}

	private Optional<Long> calculateUserNameSuffixMaxValue(final String userNameRegEx, final List<String> userNames) {
		return userNames.stream().map(userName -> {
			final Matcher matcher = Pattern.compile(userNameRegEx).matcher(userName);
			return (matcher.find() && !matcher.group(1).isEmpty()) ? Long.parseLong(matcher.group(1)) : 1;
		}).max(Long::compare);
	}

	private String getUserNameRegEx(final User user) {
		return user.getFirstName() + "\\." + user.getLastName() + "(\\d*)";
	}
}
