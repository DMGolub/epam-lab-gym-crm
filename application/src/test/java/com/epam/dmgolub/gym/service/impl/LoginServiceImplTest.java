package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@InjectMocks
	private LoginServiceImpl loginService;

	@Nested
	class TestIsValidLoginRequest {

		@Test
		void isValidLoginRequest_shouldReturnTrue_whenUserNameAndPasswordAreValid() {
			final String userName = "User.Name";
			final String password = "Password";
			final var request = new Credentials(userName, password);
			final var user = new User(1L, "User", "Name", userName, password, true);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(eq(password), any())).thenReturn(true);

			assertTrue(loginService.isValidLoginRequest(request));
			verify(userRepository, times(1)).findByUserName(userName);
		}

		@Test
		void isValidLoginRequest_shouldReturnFalse_whenUserNotFound() {
			final String userName = "User.Name";
			final String password = "Password";
			final var request = new Credentials(userName, password);

			when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

			assertFalse(loginService.isValidLoginRequest(request));
			verify(userRepository, times(1)).findByUserName(userName);
		}

		@Test
		void isValidLoginRequest_shouldReturnFalse_whenPasswordsDoNotMatch() {
			final String userName = "User.Name";
			final String password = "WrongPassword";
			final var request = new Credentials(userName, password);
			final var user = new User(1L, "User", "Name", userName, "OldPassword", true);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(eq(password), any())).thenReturn(false);

			assertFalse(loginService.isValidLoginRequest(request));
			verify(userRepository, times(1)).findByUserName(userName);
		}
	}

	@Nested
	class TestChangePassword {

		@Test
		void changePassword_shouldReturnTrue_whenUserIsFoundAndOldPasswordIsValid() {
			final String userName = "User.Name";
			final String oldPassword = "OldPassword";
			final String newPassword = "NewPassword";
			final String encodedPassword = "EncodedNewPassword";
			final var request = new ChangePasswordRequest(userName, oldPassword, newPassword);
			final User user = new User(1L, "User", "Name", userName, oldPassword, true);
			final User updatedUser = new User(1L, "User", "Name", userName, encodedPassword, true);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(eq(oldPassword), any())).thenReturn(true);
			when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

			assertTrue(loginService.changePassword(request));
			verify(userRepository, times(1)).findByUserName(userName);
			verify(userRepository, times(1)).saveAndFlush(updatedUser);
		}

		@Test
		void changePassword_shouldReturnFalse_whenUserNotFound() {
			final String userName = "User.Name";
			final String oldPassword = "OldPassword";
			final String newPassword = "NewPassword";
			final var request = new ChangePasswordRequest(userName, oldPassword, newPassword);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

			assertFalse(loginService.changePassword(request));
			verify(userRepository, times(1)).findByUserName(userName);
			verifyNoMoreInteractions(userRepository);
		}

		@Test
		void changePassword_shouldReturnFalse_whenOldPasswordsDoNotMatch() {
			final String userName = "User.Name";
			final String oldPassword = "WrongPassword";
			final String newPassword = "NewPassword";
			final var request = new ChangePasswordRequest(userName, oldPassword, newPassword);
			final User user = new User(1L, "User", "Name", userName, "OldPassword", true);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
			when(passwordEncoder.matches(eq(oldPassword), any())).thenReturn(false);

			assertFalse(loginService.changePassword(request));
			verify(userRepository, times(1)).findByUserName(userName);
			verifyNoMoreInteractions(userRepository);
		}
	}
}
