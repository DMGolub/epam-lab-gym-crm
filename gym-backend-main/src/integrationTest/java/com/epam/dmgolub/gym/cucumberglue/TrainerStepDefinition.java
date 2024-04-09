package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.TrainerRestController;
import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerUpdateRequestDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.ASSIGNED_ON_WITH_USERNAME;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.NOT_ASSIGNED_ON_WITH_USERNAME;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.PROFILE;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.PROFILE_WITH_USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TrainerStepDefinition extends AbstractStepDefinition {

	private static final int ASSIGNED_TRAINERS = 2;
	private static final int NOT_ASSIGNED_TRAINERS = 8;
	private static final String SPECIALIZATION_1 = "api/v1/training-types/1";

	public TrainerStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
	}

	@When("user submits valid new trainer data with firstName {string} and lastName {string}")
	public void unauthenticatedSubmitsValidNewTrainerData(final String firstName, final String lastName) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL;
		final var trainer = new TrainerCreateRequestDTO(firstName, lastName, SPECIALIZATION_1);
		final var httpEntity = new HttpEntity<>(trainer);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, SignUpResponseDTO.class);
	}

	@Then("server should handle request and return status code of {int}")
	public void unauthenticatedReceivesStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("the user receives new trainer credentials with userName {string}")
	public void userReceivesNewTrainerCredentials(final String userName) {
		final var trainerUserName = ((SignUpResponseDTO) Objects.requireNonNull(lastResponse.getBody()))
			.getCredentials().getUserName();
		final var password = Objects.requireNonNull((SignUpResponseDTO) lastResponse.getBody())
			.getCredentials().getPassword();
		assertEquals(userName, trainerUserName);
		assertFalse(password.isBlank());
	}

	@When("user submits invalid new trainer data with firstName {string} and lastName {string}")
	public void unauthenticatedSubmitsInvalidNewTrainerData(final String firstName, final String lastName) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL;
		final var trainer = new TrainerCreateRequestDTO(firstName, lastName, SPECIALIZATION_1);
		final var httpEntity = new HttpEntity<>(trainer);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
	}

	@When("trainer {string} with password {string} wants to update own profile with firstName {string} and lastName {string}")
	public void authenticatedTrainerWantsToUpdateOwnProfile(
		final String userName,
		final String password,
		final String requestFirstName,
		final String requestLastName
	) {
		updateTrainerProfile(userName, password, userName, requestFirstName, requestLastName, TrainerResponseDTO.class);
	}

	@And("updated trainer data with firstName {string} and lastName {string} is returned")
	public void updatedTrainerDataIsReturned(final String updatedFirstName, final String updatedLastName) {
		final var updatedTrainer = Objects.requireNonNull((TrainerResponseDTO) lastResponse.getBody());
		assertEquals(updatedFirstName, updatedTrainer.getFirstName());
		assertEquals(updatedLastName, updatedTrainer.getLastName());
	}

	@When("trainer {string} with password {string} wants to update {string} with firstName {string} and lastName {string}")
	public void authenticatedTrainerWantsToUpdateAnotherTrainerProfile(
		final String userName,
		final String password,
		final String requestUserName,
		final String requestFirstName,
		final String requestLastName
	) {
		updateTrainerProfile(userName, password, requestUserName, requestFirstName, requestLastName, String.class);
	}

	@When("unauthenticated user wants to update any trainer profile")
	public void unauthenticatedUserWantsToUpdateAnyTrainerProfile() {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + PROFILE;
		final var trainee = getTrainerUpdateRequestDTO("Any.Name", "Any", "Name");
		final var httpEntity = new HttpEntity<>(trainee);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
	}

	@When("trainer {string} with password {string} wants to find own profile by userName")
	public void authenticatedTrainerWantsToFindOwnProfile(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + PROFILE_WITH_USERNAME + userName;
		final var request = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, request, TrainerResponseDTO.class);
	}

	@Then("the {string} trainer data is returned")
	public void theRequestedTrainerDataIsReturned(final String requestUserName) {
		final var traineeUserName = Objects.requireNonNull((TrainerResponseDTO) lastResponse.getBody()).getUserName();
		assertEquals(requestUserName, traineeUserName);
	}

	@When("trainer {string} with password {string} wants to find another trainer by userName {string}")
	public void authenticatedTrainerWantsToFindAnotherTrainerProfile(
		final String userName,
		final String password,
		final String requestedUserName
	) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + PROFILE_WITH_USERNAME + requestedUserName;
		final var request = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, request, TrainerResponseDTO.class);
	}

	@When("unauthenticated user wants to find a trainer by userName {string}")
	public void unauthenticatedWantsToFindTrainerByUserName(final String userName) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + PROFILE_WITH_USERNAME + userName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
	}

	@And("no trainer data is returned")
	public void noTrainerDataIsReturned() {
		assertNull(lastResponse.getBody());
	}

	@When("trainee {string} with password {string} wants to find all trainers not assigned on himself")
	public void authenticatedTraineeWantsToFindAllTrainersNotAssignedOnHimself(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + NOT_ASSIGNED_ON_WITH_USERNAME + userName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, List.class);
	}

	@And("trainers not assigned on trainee are returned")
	@SuppressWarnings("unchecked")
	public void trainersNotAssignedOnTraineeAreReturned() {
		final var trainers = (List<TraineeResponseDTO.TrainerDTO>) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(NOT_ASSIGNED_TRAINERS, trainers.size());
	}

	@When("unauthenticated user wants to find trainers not assigned on trainee {string}")
	public void unauthenticatedUserWantsToFindTrainersNotAssignedOnTrainee(final String userName) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + NOT_ASSIGNED_ON_WITH_USERNAME + userName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, List.class);
	}

	@When("trainee {string} with password {string} wants to find all trainers assigned on himself")
	public void authenticatedTraineeWantsToFindAllTrainersAssignedOnHimself(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + ASSIGNED_ON_WITH_USERNAME + userName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, httpEntity, List.class);
	}

	@And("trainers assigned on trainee are returned")
	@SuppressWarnings("unchecked")
	public void trainersAssignedOnTraineeAreReturned() {
		final var trainers = (List<TraineeResponseDTO.TrainerDTO>) Objects.requireNonNull(lastResponse.getBody());
		assertEquals(ASSIGNED_TRAINERS, trainers.size());
	}

	@When("unauthenticated user wants to find trainers assigned on trainee {string}")
	public void unauthenticatedUserWantsToFindTrainersAssignedOnTrainee(final String userName) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + ASSIGNED_ON_WITH_USERNAME + userName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, List.class);
	}

	private void updateTrainerProfile(
		final String userName,
		final String password,
		final String requestUserName,
		final String requestFirstName,
		final String requestLastName,
		final Class<?> returnType
	) {
		final var requestUrl = LOCALHOST_URL + port + TrainerRestController.URL + PROFILE;
		final var headers = getAuthHeaders(userName, password);
		final var trainer = getTrainerUpdateRequestDTO(requestUserName, requestFirstName, requestLastName);
		final var httpEntity = new HttpEntity<>(trainer, headers);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, returnType);
	}

	private TrainerUpdateRequestDTO getTrainerUpdateRequestDTO(
		final String userName,
		final String firstName,
		final String lastName
	) {
		return new TrainerUpdateRequestDTO(userName, firstName, lastName, SPECIALIZATION_1, true);
	}
}
