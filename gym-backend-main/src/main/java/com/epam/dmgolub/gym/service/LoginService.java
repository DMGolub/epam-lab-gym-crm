package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.ChangePasswordRequest;

public interface LoginService {

	boolean changePassword(ChangePasswordRequest request);
}
