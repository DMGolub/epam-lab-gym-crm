package com.epam.dmgolub.gym.controller.mvc;

import com.epam.dmgolub.gym.dto.mvc.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.CredentialsDTO;
import com.epam.dmgolub.gym.mapper.mvc.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.CHANGE_PASSWORD_REQUEST;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.PROFILE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_LOGIN_INDEX;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_PROFILE_INDEX;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.USER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private RedirectAttributes redirectAttributes;
	@Mock
	private LoginService loginService;
	@Mock
	private ModelToDtoMapper mapper;
	@InjectMocks
	private ProfileController profileController;

	@BeforeEach
	public void setUp() {
		profileController = new ProfileController(loginService, mapper);
	}

	@Test
	void getProfilePage_shouldReturnProfileIndexPageName_whenInvoked() {
		final CredentialsDTO request = new CredentialsDTO("UserName", "Password");

		assertEquals(PROFILE_INDEX_VIEW_NAME, profileController.getProfilePage(request, model));
		verify(model).addAttribute(eq(CHANGE_PASSWORD_REQUEST), any(ChangePasswordRequestDTO.class));
		verify(model).addAttribute(eq(USER_NAME), any(String.class));
	}

	@Nested
	class TestChangePassword {

		@Test
		void changePassword_shouldReturnProfileIndexPage_whenBingingResultHasErrors() {
			final var request = new ChangePasswordRequestDTO(null, null, null);
			when(bindingResult.hasErrors()).thenReturn(true);

			final var result = profileController.changePassword(request, bindingResult, redirectAttributes);

			assertEquals(PROFILE_INDEX_VIEW_NAME, result);
			verify(bindingResult, times(1)).hasErrors();
			verifyNoInteractions(loginService);
			verifyNoInteractions(redirectAttributes);
		}

		@Test
		void changePassword_shouldRedirectToLoginIndex_whenPasswordChangedSuccessfully() {
			final var requestDTO =
				new ChangePasswordRequestDTO("UserName", "OldPassword", "NewPassword");
			final var request =
				new ChangePasswordRequest("UserName", "OldPassword", "NewPassword");
			when(bindingResult.hasErrors()).thenReturn(false);
			when(mapper.mapToChangePasswordRequest(requestDTO)).thenReturn(request);
			when(loginService.changePassword(request)).thenReturn(true);

			final var result = profileController.changePassword(requestDTO, bindingResult, redirectAttributes);

			assertEquals(REDIRECT_TO_LOGIN_INDEX, result);
			verify(mapper, times(1)).mapToChangePasswordRequest(requestDTO);
			verify(loginService, times(1)).changePassword(request);
			verify(redirectAttributes).addFlashAttribute(eq(SUCCESS_MESSAGE_ATTRIBUTE), any(String.class));
		}

		@Test
		void changePassword_shouldRedirectToProfileIndex_whenPasswordChangeFailed() {
			final var requestDTO =
				new ChangePasswordRequestDTO("WrongUserName", "WrongOldPassword", "WrongNewPassword");
			final var request =
				new ChangePasswordRequest("WrongUserName", "WrongOldPassword", "WrongNewPassword");
			when(bindingResult.hasErrors()).thenReturn(false);
			when(mapper.mapToChangePasswordRequest(requestDTO)).thenReturn(request);
			when(loginService.changePassword(request)).thenReturn(false);

			final var result = profileController.changePassword(requestDTO, bindingResult, redirectAttributes);

			assertEquals(REDIRECT_TO_PROFILE_INDEX, result);
			verify(mapper, times(1)).mapToChangePasswordRequest(requestDTO);
			verify(loginService, times(1)).changePassword(request);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}
	}
}
