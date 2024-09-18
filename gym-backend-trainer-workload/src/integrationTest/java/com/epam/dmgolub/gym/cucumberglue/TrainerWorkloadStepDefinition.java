package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.KeyUtils;
import com.epam.dmgolub.gym.controller.TrainerWorkloadRestController;
import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.entity.TrainerWorkload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerWorkloadStepDefinition {

	private static final String TRAINER_USER_NAME = "Some.Trainer";
	private static final String TRAINER_FIRST_NAME = "Some";
	private static final String TRAINER_LAST_NAME = "Trainer";

	private ResponseEntity<?> lastResponse;
	@LocalServerPort
	private String port;
	private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

	private final TestRestTemplate restTemplate;
	private final MongoTemplate mongoTemplate;

	public TrainerWorkloadStepDefinition(final TestRestTemplate restTemplate, final MongoTemplate mongoTemplate) {
		this.restTemplate = restTemplate;
		this.mongoTemplate = mongoTemplate;
	}

	@When("service submits valid trainer workload {string} request with duration of {int}")
	public void serviceSubmitsValidTrainerWorkloadUpdateRequestWithDuration(final String actionType, final int duration)
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, ParseException {
		final var headers = getHttpHeadersWithBearerAuth();
		final var trainingRequest = getTrainerWorkloadUpdateRequestDTO(
			TRAINER_USER_NAME, TRAINER_FIRST_NAME, TRAINER_LAST_NAME, duration, actionType);
		final var httpEntity = new HttpEntity<>(trainingRequest, headers);
		final var requestUrl = LOCALHOST_URL + port + TrainerWorkloadRestController.URL + UPDATE;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
	}

	@Then("application should handle request and return status code of {int}")
	public void applicationShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("service gets message {string}")
	public void serviceGetsMessage(final String expectedMessage) {
		final String receivedMessage = (String) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(expectedMessage, receivedMessage);
	}

	@And("database should contain the expected record with duration of {int}")
	public void databaseShouldContainTheExpectedRecord(final int duration) {
		final var workload = mongoTemplate.findAll(TrainerWorkload.class, "workload");
		final var expected = new TrainerWorkload(
			TRAINER_USER_NAME,
			TRAINER_FIRST_NAME,
			TRAINER_LAST_NAME,
			true,
			List.of(new TrainerWorkload.Year(2025, List.of(new TrainerWorkload.Month(1, duration))))
		);
		assertEquals(1, workload.size());
		assertEquals(expected, workload.get(0));
	}

	@When("unauthenticated service submits valid trainer workload add request")
	public void unauthenticatedServiceSubmitsValidTrainerWorkloadAddRequest() throws ParseException {
		final var trainingRequest = getTrainerWorkloadUpdateRequestDTO(
			TRAINER_USER_NAME, TRAINER_FIRST_NAME, TRAINER_LAST_NAME, 60, "ADD");
		final var httpEntity = new HttpEntity<>(trainingRequest);
		final var requestUrl = LOCALHOST_URL + port + TrainerWorkloadRestController.URL + UPDATE;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
	}

	private HttpHeaders getHttpHeadersWithBearerAuth()
		throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		final var privateKey = KeyUtils.getPrivateKey("private.pem");
		final var jwt = Jwts.builder()
			.setSubject("test")
			.claim("SCOPE", "message.write")
			.setIssuedAt(new Date())
			.signWith(privateKey)
			.compact();
		final var headers = new HttpHeaders();
		headers.setBearerAuth(jwt);
		return headers;
	}

	private TrainerWorkloadUpdateRequestDTO getTrainerWorkloadUpdateRequestDTO(
		final String trainerUserName,
		final String trainerFirstName,
		final String trainerLastName,
		final int duration,
		final String actionType
	) throws ParseException {
		return new TrainerWorkloadUpdateRequestDTO(trainerUserName, trainerFirstName, trainerLastName, true,
			formatter.parse("1-Jan-2025"), duration, actionType);
	}
}
