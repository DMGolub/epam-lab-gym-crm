package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.LoginRequestDTO;
import com.epam.dmgolub.gym.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;
import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_LOGIN_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_PROFILE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

	@Mock
	private BindingResult bindingResult;
	@Mock
	private RedirectAttributes redirectAttributes;
	@Mock
	private HttpSession session;
	@Mock
	private LoginService loginService;
	@InjectMocks
	private LoginController loginController;

	@BeforeEach
	void setUp() {
		loginController = new LoginController(loginService);
	}

	@Test
	void getLoginPage_shouldReturnLoginIndexPageName_whenInvoked() {
		assertEquals(LOGIN_INDEX_VIEW_NAME, loginController.getLoginPage());
	}

	@Nested
	class LogInTest {

		@Test
		void logIn_shouldReturnLoginIndexPage_whenBingingResultHasErrors() {
			final LoginRequestDTO request = new LoginRequestDTO(null, null);
			when(bindingResult.hasErrors()).thenReturn(true);

			final var result = loginController.logIn(request, bindingResult, session, redirectAttributes);

			assertEquals(LOGIN_INDEX_VIEW_NAME, result);
			verify(bindingResult, times(1)).hasErrors();
			verifyNoInteractions(loginService);
			verifyNoInteractions(session);
		}


		@Test
		void logIn_shouldRedirectToProfileIndex_whenUserAuthenticatedSuccessfully() {
			final LoginRequestDTO request = new LoginRequestDTO("ValidUserName", "ValidPassword");
			when(bindingResult.hasErrors()).thenReturn(false);
			when(loginService.isValidLoginRequest(request)).thenReturn(true);

			final var result = loginController.logIn(request, bindingResult, session, redirectAttributes);

			assertEquals(REDIRECT_TO_PROFILE_INDEX, result);
			verify(bindingResult, times(1)).hasErrors();
			verify(loginService, times(1)).isValidLoginRequest(request);
			verify(redirectAttributes).addFlashAttribute(eq(SUCCESS_MESSAGE_ATTRIBUTE), any(String.class));
			verify(session).setAttribute(eq(LOGIN), any(LoginRequestDTO.class));
		}

		@Test
		void logIn_shouldReturnErrorMessage_whenUserAuthenticationFailed() {
			final LoginRequestDTO request = new LoginRequestDTO("UserName", "WrongPassword");
			when(bindingResult.hasErrors()).thenReturn(false);
			when(loginService.isValidLoginRequest(request)).thenReturn(false);

			final var result = loginController.logIn(request, bindingResult, session, redirectAttributes);

			assertEquals(REDIRECT_TO_LOGIN_INDEX, result);
			verify(bindingResult, times(1)).hasErrors();
			verify(loginService, times(1)).isValidLoginRequest(request);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
			verifyNoInteractions(session);
		}
	}

	@Test
	void logOut_shouldInvalidateSession_whenInvoked() {
		final var result = loginController.logOut(session, redirectAttributes);

		assertEquals(REDIRECT_TO_LOGIN_INDEX, result);
		verify(session, times(1)).invalidate();
		verify(redirectAttributes).addFlashAttribute(eq(SUCCESS_MESSAGE_ATTRIBUTE), any(String.class));
	}
}
