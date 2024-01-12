package com.epam.dmgolub.gym.interceptor;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.epam.dmgolub.gym.controller.constant.Constants.LOGIN;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginInterceptor.class);

	private final LoginService loginService;
	private final ModelToDtoMapper mapper;

	public LoginInterceptor(final LoginService loginService, final ModelToDtoMapper mapper) {
		this.loginService = loginService;
		this.mapper = mapper;
	}

	@Override
	public boolean preHandle(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final Object handler
	) throws Exception {
		String sessionId = null;
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("JSESSIONID".equals(cookie.getName())) {
					sessionId = cookie.getValue();
				}
			}
			LOGGER.debug("In preHandle - Incoming request session id={} for URI={}", sessionId, request.getRequestURI());
		}

		final CredentialsDTO login = (CredentialsDTO) request.getSession().getAttribute(LOGIN);
		final var credentials = mapper.credentialsDTOTOCredentials(login);
		if (login != null && loginService.isValidLoginRequest(credentials)) {
			LOGGER.debug("In preHandle - Successfully authenticated user={}", login.getUserName());
			return true;
		} else {
			LOGGER.debug("In preHandle - Failed to authenticate user");
			response.sendRedirect("../login?error=unauthorized");
			return false;
		}
	}
}
