package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.entity.User;

public interface UserCredentialsGenerator {

	public String generateUserName(User user);
	public String generatePassword(User user);
}
