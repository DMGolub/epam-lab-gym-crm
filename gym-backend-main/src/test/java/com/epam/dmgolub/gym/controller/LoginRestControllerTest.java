package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.security.service.TokenService;
import com.epam.dmgolub.gym.service.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginRestControllerTest {

	private static final String URL = LoginRestController.URL;

	@Mock
	private LoginService loginService;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private TokenService tokenService;
	@Mock
	private ModelToRestDtoMapper mapper;
	@Mock
	private Authentication authentication;
	@InjectMocks
	private LoginRestController loginRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(loginRestController).build();
	}

	@Test
	void logIn_shouldReturnOk_whenCredentialsAreValid() throws Exception {
		final String userName = "User.Name";
		final String password = "Password";
		final var credentials = new CredentialsDTO(userName, password);
		final var usernamePasswordToken =
			new UsernamePasswordAuthenticationToken(credentials.getUserName(), credentials.getPassword());
		when(authenticationManager.authenticate(usernamePasswordToken)).thenReturn(authentication);
		when(tokenService.generateToken(userName)).thenReturn("generated.jwt.token");

		mockMvc.perform(post(URL + "/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(credentials)))
			.andExpect(status().isOk());

		verify(authenticationManager, times(1)).authenticate(usernamePasswordToken);
		verify(tokenService, times(1)).generateToken(userName);
		verifyNoMoreInteractions(authenticationManager);
	}

	@Nested
	class TestChangePassword {

		@Test
		void changePassword_shouldReturnOk_whenCredentialsAreValid() throws Exception {
			final String userName = "User.Name";
			final String oldPassword = "OldPassword";
			final String newPassword = "NewPassword";
			final var requestDTO = new ChangePasswordRequestDTO(userName, oldPassword, newPassword);
			final var request = new ChangePasswordRequest(userName, oldPassword, newPassword);
			when(mapper.mapToChangePasswordRequest(requestDTO)).thenReturn(request);
			when(loginService.changePassword(request)).thenReturn(true);
			mockMvc.perform(put(URL + "/change-password")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(requestDTO)))
				.andExpect(status().isOk());

			verify(mapper, times(1)).mapToChangePasswordRequest(requestDTO);
			verify(loginService, times(1)).changePassword(request);
			verifyNoMoreInteractions(loginService);
		}

		@Test
		void changePassword_shouldReturnInternalServerError_whenCredentialsAreInvalid() throws Exception {
			final String userName = "User.Name";
			final String oldPassword = "OldPassword";
			final String newPassword = "NewPassword";
			final var requestDTO = new ChangePasswordRequestDTO(userName, oldPassword, newPassword);
			final var request = new ChangePasswordRequest(userName, oldPassword, newPassword);
			when(mapper.mapToChangePasswordRequest(requestDTO)).thenReturn(request);
			when(loginService.changePassword(request)).thenReturn(false);
			mockMvc.perform(put(URL + "/change-password")
					.contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(requestDTO)))
				.andExpect(status().isInternalServerError());

			verify(mapper, times(1)).mapToChangePasswordRequest(requestDTO);
			verify(loginService, times(1)).changePassword(request);
			verifyNoMoreInteractions(loginService);
		}
	}
}
