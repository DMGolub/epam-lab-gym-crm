package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.TrainingTypeRestController;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingTypeStepDefinition extends AbstractStepDefinition {

	private static final int TRAINING_TYPES_COUNT = 10;

	protected TrainingTypeStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	@When("user wants to get all training types")
	public void userWantsToGetAllTrainingTypes() {
		final var requestUrl = LOCALHOST_URL + port + TrainingTypeRestController.URL;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, List.class);
	}

	@Then("application should handle request and return status code of {int}")
	public void applicationShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("user receives all training types")
	@SuppressWarnings("unchecked")
	public void userReceivesAllTrainingTypes() {
		final var trainingTypes = (List<TrainingTypeDTO>) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(TRAINING_TYPES_COUNT, trainingTypes.size());
	}

	@When("user wants to get training type by id {int}")
	public void userWantsToGetTrainingTypeById(final int id) {
		final var requestUrl = LOCALHOST_URL + port + TrainingTypeRestController.URL + "/" + id;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, TrainingTypeDTO.class);
	}

	@And("user receives training type with id {int}")
	public void userReceivesTrainingTypeWithId(final int id) {
		final var trainingType = (TrainingTypeDTO) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(id, trainingType.getId());
	}

	@When("user wants to get not existing training type by id {int}")
	public void userWantsToGetNotExistingTrainingTypeById(final int id) {
		final var requestUrl = LOCALHOST_URL + port + TrainingTypeRestController.URL + "/" + id;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
	}
}
