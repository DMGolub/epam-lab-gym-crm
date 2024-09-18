package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.UserModel;

import java.util.List;

public interface UserService {

	List<UserModel> findAll();

	void changeActivityStatus(String userName);
}
