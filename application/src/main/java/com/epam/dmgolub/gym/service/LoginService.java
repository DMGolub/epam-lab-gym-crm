package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.LoginRequestDTO;

public interface LoginService {

	boolean isValidLoginRequest(LoginRequestDTO request);

	boolean changePassword(ChangePasswordRequestDTO request);
}
