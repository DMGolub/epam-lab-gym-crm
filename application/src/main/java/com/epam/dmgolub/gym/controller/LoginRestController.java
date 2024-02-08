package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.exception.PasswordChangeFailedException;
import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.security.service.TokenService;
import com.epam.dmgolub.gym.security.utility.SecurityUtils;
import com.epam.dmgolub.gym.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import static com.epam.dmgolub.gym.controller.LoginRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class LoginRestController {

	public static final String URL = BASE_API_URL + ApiVersion.VERSION_1;
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

	private final LoginService loginService;
	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final ModelToRestDtoMapper mapper;
	private final SecurityContextLogoutHandler logoutHandler;

	public LoginRestController(
		final LoginService loginService,
		final AuthenticationManager authenticationManager,
		final TokenService tokenService,
		final ModelToRestDtoMapper mapper
	) {
		this.loginService = loginService;
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.mapper = mapper;
		logoutHandler = new SecurityContextLogoutHandler();
	}

	@PostMapping(value = "/login", consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "User authentication")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User authenticated successfully"),
		@ApiResponse(responseCode = "401", description = "User authentication failed"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<String> logIn(@RequestBody @Valid final CredentialsDTO credentials) {
		final String userName = credentials.getUserName();
		LOGGER.debug("In logIn - Received a request to authenticate user={}", userName);
		final var authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(userName, credentials.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final String token = tokenService.generateToken(userName);
		LOGGER.debug("In logIn - User authenticated successfully as {}", userName);
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

	@PutMapping(value = "/change-password", consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "User password changing")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User password changed successfully"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<String> changePassword(@RequestBody @Valid final ChangePasswordRequestDTO request) {
		final String userName = request.getUserName();
		LOGGER.debug("In changePassword - Received request to change password for user={}", userName);
		if (loginService.changePassword(mapper.mapToChangePasswordRequest(request))) {
			final String message = "Password changed successfully for user=" + userName;
			LOGGER.debug("In changePassword - {}", message);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} else {
			final String message = "Failed to change password for user=" + userName;
			LOGGER.debug("In changePassword - {}", message);
			throw new PasswordChangeFailedException(message);
		}
	}

	@PostMapping("/logout")
	@Operation(summary = "Log out user with token invalidation")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User logged out successfully"),
		@ApiResponse(responseCode = "401", description = "User authentication failed"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<String> logOut(
		final Authentication authentication,
		final HttpServletRequest request,
		final HttpServletResponse response
	) {
		final String userName = authentication.getName();
		LOGGER.debug("In logout - Received a request to log out user with userName={}", userName);
		SecurityUtils.extractBearerToken(request).ifPresent(tokenService::denyToken);
		logoutHandler.logout(request, response, authentication);
		final String message = "Successfully logged out user with userName=" + userName;
		LOGGER.debug("In logout - {}", message);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
