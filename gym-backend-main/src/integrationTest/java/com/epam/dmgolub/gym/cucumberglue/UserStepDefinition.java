package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.UserRestController;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.PROFILE_WITH_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserStepDefinition extends AbstractStepDefinition {

	protected UserStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	@When("user {string} with password {string} changes status of {string}")
	public void userChangesActivityStatus(final String userName, final String password, final String requestUserName) {
		final var requestUrl = UserRestController.URL + PROFILE_WITH_USERNAME + requestUserName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PATCH, httpEntity, Void.class);
	}

	@Then("app should handle request and return status code of {int}")
	public void appShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@When("unauthenticated user changes status of {string}")
	public void unauthenticatedUserChangesActivityStatus(final String userName) {
		final var requestUrl = UserRestController.URL + PROFILE_WITH_USERNAME + userName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PATCH, null, Void.class);
	}
}
