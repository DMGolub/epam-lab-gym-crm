package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;

public interface LoginService {

	boolean isValidLoginRequest(CredentialsDTO credentialsDTO);

	boolean changePassword(ChangePasswordRequestDTO requestDTO);
}
