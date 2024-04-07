package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.epam.dmgolub.gym.controller.UserRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class UserRestController {

	public static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/users";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	private final UserService userService;

	public UserRestController(final UserService userService) {
		this.userService = userService;
	}

	@PatchMapping("/profile")
	@Operation(summary = "Change activity status by the supplied username")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully changed user activity status"),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The user you were trying to update is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	@ResponseStatus(HttpStatus.OK)
	public void changeActivityStatus(@RequestParam("userName") final String userName) {
		LOGGER.debug("[{}] In changeActivityStatus - Received a request to change status for user={}",
			MDC.get(TRANSACTION_ID), userName);
		userService.changeActivityStatus(userName);
	}
}
