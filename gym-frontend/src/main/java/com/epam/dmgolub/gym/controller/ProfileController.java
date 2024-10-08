package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.utility.ControllerUtilities;
import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.service.LoginService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.dmgolub.gym.controller.constant.Constants.CHANGE_PASSWORD_REQUEST;
import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.PROFILE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_LOGIN_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_PROFILE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.USER_NAME;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

	private final LoginService loginService;

	public ProfileController(final LoginService loginService) {
		this.loginService = loginService;
	}

	@GetMapping("/")
	public String getProfilePage(
		@SessionAttribute(USER_NAME) @NotNull final String userName,
		final Model model
	) {
		model.addAttribute(CHANGE_PASSWORD_REQUEST, new ChangePasswordRequestDTO());
		model.addAttribute(USER_NAME, userName);
		LOGGER.debug("In getProfilePage - Returning profile index view name");
		return PROFILE_INDEX_VIEW_NAME;
	}

	@PutMapping
	public String changePassword(
		@ModelAttribute(CHANGE_PASSWORD_REQUEST) @Valid final ChangePasswordRequestDTO request,
		final BindingResult bindingResult,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In changePassword - Received request to change password for user={}", request.getUserName());
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, PROFILE_INDEX_VIEW_NAME);
			return PROFILE_INDEX_VIEW_NAME;
		}
		if (loginService.changePassword(request)) {
			redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE,
				"Your password was changed successfully. Please log in with new password");
			return REDIRECT_TO_LOGIN_INDEX;
		} else {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE,
				"Could not change your password, please try again");
			return REDIRECT_TO_PROFILE_INDEX;
		}
	}
}
