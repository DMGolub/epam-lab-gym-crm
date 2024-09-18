package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.TrainingRestController;
import com.epam.dmgolub.gym.dto.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.SEARCH_BY_TRAINEE;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.SEARCH_BY_TRAINER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingStepDefinition extends AbstractStepDefinition {

	protected TrainingStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	@When("trainee {string} with password {string} wants to get all own trainings")
	public void traineeWantsToGetAllOwnTrainings(final String userName, final String password) {
		final var requestUrl =
			LOCALHOST_URL + port + TrainingRestController.URL + SEARCH_BY_TRAINEE + "?traineeUserName=" + userName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, List.class);
	}

	@Then("the application should handle request and return status code of {int}")
	public void applicationShouldHandleRequestAndReturnStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("user receives all the required trainee trainings")
	@SuppressWarnings("unchecked")
	public void userReceivesAllTheRequiredTraineeTrainings() {
		final var trainings = (List<TraineeTrainingResponseDTO>) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(1, trainings.size());
	}

	@When("unauthenticated user wants to get trainings by trainee {string}")
	public void unauthenticatedUserWantsToGetTrainingsByTrainee(final String traineeUserName) {
		final var requestUrl =
			LOCALHOST_URL + port + TrainingRestController.URL + SEARCH_BY_TRAINEE + "?traineeUserName=" + traineeUserName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, List.class);
	}

	@When("trainer {string} with password {string} wants to get all own trainings")
	public void trainerWantsToGetAllOwnTrainings(final String userName, final String password) {
		final var requestUrl =
			LOCALHOST_URL + port + TrainingRestController.URL + SEARCH_BY_TRAINER + "?trainerUserName=" + userName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, List.class);
	}

	@And("user receives all the required trainer trainings")
	@SuppressWarnings("unchecked")
	public void userReceivesAllTheRequiredTrainerTrainings() {
		final var trainings = (List<TrainerTrainingResponseDTO>) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(1, trainings.size());
	}

	@When("unauthenticated user wants to get trainings by trainer {string}")
	public void unauthenticatedUserWantsToGetTrainingsByTrainer(final String trainerUserName) {
		final var requestUrl =
			LOCALHOST_URL + port + TrainingRestController.URL + SEARCH_BY_TRAINER + "?trainerUserName=" + trainerUserName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, List.class);
	}

	@When("trainee {string} with password {string} submits valid data for a new training with {string}")
	public void traineeSubmitsValidDataForANewTraining(
		final String traineeUserName,
		final String traineePassword,
		final String trainerUserName
	) throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TrainingRestController.URL;
		final var request = getTrainingCreateRequestDTO(traineeUserName, trainerUserName, "NewTraining", 90);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(traineeUserName, traineePassword));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Void.class);
	}

	@When("trainer {string} with password {string} submits data for a new training with {string}")
	public void trainerSubmitsDataForANewTraining(
		final String trainerUserName,
		final String trainerPassword,
		final String traineeUserName
	) throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TrainingRestController.URL;
		final var request = getTrainingCreateRequestDTO(traineeUserName, trainerUserName, "Training", 45);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(trainerUserName, trainerPassword));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Void.class);
	}

	@When("unauthenticated user wants to add new training")
	public void unauthenticatedUserWantsToAddNewTraining() throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TrainingRestController.URL;
		final var request = getTrainingCreateRequestDTO("Trainee", "Trainer", "NewTraining", 30);
		final var httpEntity = new HttpEntity<>(request);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, Void.class);
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
			formatter.parse("1-Jan-2030"),
			duration
		);
	}
}
