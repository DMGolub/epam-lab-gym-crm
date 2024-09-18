package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.TrainingRestController;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.entity.TrainerWorkload;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.awaitility.Awaitility;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.epam.dmgolub.gym.constant.Constants.LOCALHOST_URL;
import static com.epam.dmgolub.gym.cucumberglue.SpringIntegrationTest.MAIN_SERVICE_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MicroservicesIntegrationStepDefinition {

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	private ResponseEntity<?> lastResponse;

	private final TestRestTemplate restTemplate;
	private final MongoTemplate mongoTemplate;

	public MicroservicesIntegrationStepDefinition(
		final TestRestTemplate restTemplate,
		final MongoTemplate mongoTemplate
	) {
		this.restTemplate = restTemplate;
		this.mongoTemplate = mongoTemplate;
	}

	@When("trainee {string} with password {string} adds new training with {string} and duration of {int}")
	public void traineeAddsNewTraining(
		final String traineeUserName,
		final String traineePassword,
		final String trainerUserName,
		final int duration
	) throws ParseException {
		final var requestUrl = LOCALHOST_URL + SpringIntegrationTest.mainService.getMappedPort(MAIN_SERVICE_PORT)
			+ TrainingRestController.URL;
		final var request = getTrainingCreateRequestDTO(traineeUserName, trainerUserName, "NewTraining", duration);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(traineeUserName, traineePassword));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Void.class);
	}

	@Then("application should handle request and return status code of {int}")
	public void applicationShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("workload database should contain a record for {string} with total duration of {int}")
	public void databaseShouldContainTheExpectedRecord(final String trainerUserName, final int duration) {
		final var expected = new TrainerWorkload(
			trainerUserName,
			getFirstName(trainerUserName),
			getLastName(trainerUserName),
			true,
			List.of(new TrainerWorkload.Year(2025, List.of(new TrainerWorkload.Month(1, duration))))
		);

		Awaitility.await().atMost(60, TimeUnit.SECONDS).until(() -> {
			final var workload = mongoTemplate.findAll(TrainerWorkload.class, "workload");
			return workload.size() == 1 && expected.equals(workload.get(0));
		});
	}

	private String getFirstName(final String userName) {
		return userName.substring(0, userName.indexOf('.'));
	}

	private String getLastName(final String userName) {
		return userName.substring(userName.indexOf('.') + 1);
	}

	private TrainingCreateRequestDTO getTrainingCreateRequestDTO(
		final String traineeUserName,
		final String trainerUserName,
		final String name,
		final int duration
	) throws ParseException {
		return new TrainingCreateRequestDTO(
			traineeUserName,
			trainerUserName,
			name,
			formatter.parse("15-Jan-2025"),
			duration
		);
	}

	private HttpHeaders getAuthHeaders(final String userName, final String password) {
		final var headers = new HttpHeaders();
		headers.setBasicAuth(userName, password);
		return headers;
	}
}
