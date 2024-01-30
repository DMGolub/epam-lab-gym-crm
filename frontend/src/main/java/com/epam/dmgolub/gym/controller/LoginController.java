package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.utility.ControllerUtilities;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;
import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_LOGIN_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_PROFILE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;

@Controller
@RequestMapping("login")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	private final LoginService loginService;

	public LoginController(final LoginService loginService) {
		this.loginService = loginService;
	}

	@ModelAttribute(LOGIN)
	public CredentialsDTO getDefaultLogin() {
		return new CredentialsDTO();
	}

	@GetMapping
	public String getLoginPage() {
		LOGGER.debug("In getLoginPage - Returning login index view name");
		return LOGIN_INDEX_VIEW_NAME;
	}

	@GetMapping("/log-in")
	public String logIn(
		@ModelAttribute(LOGIN) @Valid final CredentialsDTO request,
		final BindingResult bindingResult,
		final HttpSession session,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In logIn - Received a request to authenticate user={}", request.getUserName());
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, LOGIN_INDEX_VIEW_NAME);
			return LOGIN_INDEX_VIEW_NAME;
		}
		if (loginService.isValidLoginRequest(request)) {
			session.setAttribute(LOGIN, request);
			redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE,
				"You authenticated successfully as " + request.getUserName());
			return REDIRECT_TO_PROFILE_INDEX;
		} else {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE,
				"Failed to authenticate. Wrong user name or password");
			return REDIRECT_TO_LOGIN_INDEX;
		}
	}

	@GetMapping("/log-out")
	public String logOut(final HttpSession session, final RedirectAttributes redirectAttributes) {
		LOGGER.debug("In logOut - Received a request to log out");
		session.invalidate();
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE, "You successfully logged out");
		return REDIRECT_TO_LOGIN_INDEX;
	}
}
