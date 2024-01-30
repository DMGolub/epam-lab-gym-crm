package com.epam.dmgolub.gym.interceptor;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;

@Component
public class SessionLoginInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionLoginInterceptor.class);

	private LoginService loginService;

	public void setLoginService(final LoginService loginService) {
		this.loginService = loginService;
	}

	@Override
	public boolean preHandle(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler
	) throws Exception {
		final CredentialsDTO login = (CredentialsDTO) request.getSession().getAttribute(LOGIN);
		if (login != null && loginService.isValidLoginRequest(login)) {
			LOGGER.debug("In preHandle - Successfully authenticated user={}", login.getUserName());
			return true;
		} else {
			LOGGER.debug("In preHandle - Failed to authenticate user");
			response.sendRedirect("../login?error=unauthorized");
			return false;
		}
	}
}
