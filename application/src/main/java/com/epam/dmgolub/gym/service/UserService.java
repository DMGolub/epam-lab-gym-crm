package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.UserModel;

public interface UserService extends BaseService<Long, UserModel> {

	void changeActivityStatus(String userName);
}
