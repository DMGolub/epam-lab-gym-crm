package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.epam.dmgolub.gym.controller.rest.UserRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class UserRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/users";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

	private final UserService userService;

	public UserRestController(final UserService userService) {
		this.userService = userService;
	}

	@PatchMapping("/profile")
	@ResponseStatus(HttpStatus.OK)
	public void changeActivityStatus(@RequestParam("userName") final String userName) {
		LOGGER.debug("In changeActivityStatus - Received a request to change status for user={}", userName);
		userService.changeActivityStatus(userName);
	}
}
