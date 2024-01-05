package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserCredentialsGeneratorImpl implements UserCredentialsGenerator {

	private static final String POSSIBLE_PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
		"abcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserCredentialsGeneratorImpl.class);

	@Value("${password.generated.length}")
	private static int passwordLength;

	private final UserRepository userRepository;

	public UserCredentialsGeneratorImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public String generateUserName(final User user) {
		LOGGER.debug("In generateUserName - generating user name for {}", user);
		final String userNameRegEx = getUserNameRegEx(user);
		final var similarUserNames = findSimilarUserNames(userNameRegEx);
		final Optional<Long> suffixMaxValue = calculateUserNameSuffixMaxValue(userNameRegEx, similarUserNames);
		final String userName = user.getFirstName() + "." + user.getLastName();
		return suffixMaxValue.map(value -> userName + (value + 1)).orElse(userName);
	}

	@Override
	public String generatePassword(final User user) {
		LOGGER.debug("In generateUserName - generating password for {}", user);
		char[] chars = POSSIBLE_PASSWORD_CHARACTERS.toCharArray();
		return RandomStringUtils.random(passwordLength, 0, chars.length - 1, false, false, chars, new SecureRandom());
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
