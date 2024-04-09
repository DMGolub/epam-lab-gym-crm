package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.LoginRestController;
import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginStepDefinition extends AbstractStepDefinition {

	protected LoginStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	@When("user {string} with password {string} submits credentials")
	public void userSubmitsValidCredentials(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + LoginRestController.URL + "/login";
		final var credentials = new CredentialsDTO(userName, password);
		final var httpEntity = new HttpEntity<>(credentials);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
	}

	@Then("the app should handle request and return status code of {int}")
	public void appShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("user receives access token")
	public void userReceivesAccessToken() {
		final var token = (String) Objects.requireNonNull(lastResponse.getBody());
		assertFalse(token.isBlank());
	}

	@When("user {string} with password {string} submits valid password change request")
	public void submitsValidPasswordChangeRequest(final String userName, final String password) {
		sendPasswordChangeRequest(userName, password, "NewPassword");
	}

	@And("user receives a message {string}")
	public void userReceivesAMessage(final String expectedMessage) {
		final var response = (String) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(expectedMessage, response);
	}

	@When("user {string} with password {string} submits change request with invalid password")
	public void userSubmitsChangeRequestWithInvalidPassword(final String userName, final String password) {
		sendPasswordChangeRequest(userName, password, "");
	}

	private void sendPasswordChangeRequest(final String userName, final String oldPassword, final String newPassword) {
		final var requestUrl = LOCALHOST_URL + port + LoginRestController.URL + "/change-password";
		final var request = new ChangePasswordRequestDTO(userName, oldPassword, newPassword);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(userName, oldPassword));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
	}
}
