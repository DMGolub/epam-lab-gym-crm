package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.model.Credentials;

public interface LoginService {

	boolean isValidLoginRequest(Credentials request);

	boolean changePassword(ChangePasswordRequest request);
}
