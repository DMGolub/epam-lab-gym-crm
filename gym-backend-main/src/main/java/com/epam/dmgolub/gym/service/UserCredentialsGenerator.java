package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.entity.User;

public interface UserCredentialsGenerator {

	String generateUserName(User user);

	String generatePassword(User user);

	String encodePassword(String password);
}
