package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.rest.exception.PasswordChangeFailedException;
import com.epam.dmgolub.gym.dto.rest.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.rest.CredentialsDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import static com.epam.dmgolub.gym.controller.rest.LoginRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class LoginRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/login";
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginRestController.class);

	private final LoginService loginService;
	private final ModelToRestDtoMapper mapper;

	public LoginRestController(final LoginService loginService, final ModelToRestDtoMapper mapper) {
		this.loginService = loginService;
		this.mapper = mapper;
	}

	@GetMapping(consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "User authentication")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "User authenticated successfully"),
		@ApiResponse(responseCode = "401", description = "User authentication failed"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<String> logIn(@RequestBody @Valid final CredentialsDTO request) {
		final String userName = request.getUserName();
		LOGGER.debug("In logIn - Received a request to authenticate user={}", userName);
		if (loginService.isValidLoginRequest(mapper.mapToCredentials(request))) {
			final String message = "User authenticated successfully as " + userName;
			LOGGER.debug("In logIn - {}", message);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} else {
			final String message = "Failed to authenticate user=" + userName + ": wrong user name or password";
			LOGGER.debug("In logIn - {}", message);
			return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping(consumes = APPLICATION_JSON_VALUE)
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
}
