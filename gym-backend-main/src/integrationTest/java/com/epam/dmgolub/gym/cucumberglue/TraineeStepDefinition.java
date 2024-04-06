package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.controller.TraineeRestController;
import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeUpdateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeUpdateTrainerListRequestDTO;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.LOCALHOST_URL;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.PROFILE;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.PROFILE_WITH_USERNAME;
import static com.epam.dmgolub.gym.cucumberglue.constant.Constants.UPDATE_TRAINERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TraineeStepDefinition extends AbstractStepDefinition {

	private final List<String> trainerNamesToBeUpdated;

	public TraineeStepDefinition(final TestRestTemplate restTemplate) {
		super(restTemplate);
		trainerNamesToBeUpdated = List.of("Kylian.Garcia", "Ezrah.Jackson", "Eren.Miller");
	}

	@When("user submits valid new trainee data with firstName {string} and lastName {string}")
	public void unauthenticatedSubmitsValidNewTraineeData(final String firstName, final String lastName) throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL;
		final var trainee =
			new TraineeCreateRequestDTO(firstName, lastName, formatter.parse("7-Jun-2005"), "Address");
		final var httpEntity = new HttpEntity<>(trainee);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, SignUpResponseDTO.class);
	}

	@Then("the server should handle request and return status code of {int}")
	public void unauthenticatedReceivesStatusCode(final int statusCode) {
		assertEquals(HttpStatusCode.valueOf(statusCode), lastResponse.getStatusCode());
	}

	@And("the user receives new trainee credentials with userName {string}")
	public void userReceivesNewTraineeCredentials(final String userName) {
		final var traineeUserName = ((SignUpResponseDTO) Objects.requireNonNull(lastResponse.getBody()))
			.getCredentials().getUserName();
		final var password = Objects.requireNonNull((SignUpResponseDTO) lastResponse.getBody())
			.getCredentials().getPassword();
		assertEquals(userName, traineeUserName);
		assertFalse(password.isBlank());
	}

	@When("user submits invalid new trainee data with firstName {string} and lastName {string}")
	public void unauthenticatedSubmitsInvalidNewTraineeData(final String firstName, final String lastName) throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL;
		final var trainee =
			new TraineeCreateRequestDTO(firstName, lastName, formatter.parse("7-Jun-2025"), "Address");
		final var httpEntity = new HttpEntity<>(trainee);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity, String.class);
	}

	@And("no data is returned")
	public void noDataIsReturned() {
		assertNull(lastResponse.getBody());
	}

	@When("trainee {string} with password {string} wants to update own profile with firstName {string} and lastName {string}")
	public void authenticatedTraineeWantsToUpdateOwnProfileByUserName(
		final String userName,
		final String password,
		final String requestFirstName,
		final String requestLastName
	) throws ParseException {
		updateTraineeProfile(userName, password, userName, requestFirstName, requestLastName, TraineeResponseDTO.class);
	}

	@When("trainee {string} with password {string} wants to update {string} with firstName {string} and lastName {string}")
	public void authenticatedTraineeWantsToUpdateAnotherTraineeProfile(
		final String userName,
		final String password,
		final String requestUserName,
		final String requestFirstName,
		final String requestLastName
	) throws ParseException {
		updateTraineeProfile(userName, password, requestUserName, requestFirstName, requestLastName, String.class);
	}

	@When("unauthenticated user wants to update any trainee profile")
	public void unauthenticatedUserWantsToUpdateAnyTraineeProfile() throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE;
		final var trainee = getTraineeUpdateRequestDTO("Any.Name", "Any", "Name");
		final var httpEntity = new HttpEntity<>(trainee);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
	}

	@And("updated trainee data with firstName {string} and lastName {string} is returned")
	public void updatedTraineeDataIsReturned(final String updatedFirstName, final String updatedLastName) {
		final var updatedTrainee = Objects.requireNonNull((TraineeResponseDTO) lastResponse.getBody());
		assertEquals(updatedFirstName, updatedTrainee.getFirstName());
		assertEquals(updatedLastName, updatedTrainee.getLastName());
	}

	@When("trainee {string} with password {string} wants to find own profile by userName")
	public void authenticatedTraineeWantsToFindOwnProfile(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + userName;
		final var request = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, request, TraineeResponseDTO.class);
	}

	@Then("the {string} trainee data is returned")
	public void theRequestedTraineeDataIsReturned(final String requestUserName) {
		final var traineeUserName = Objects.requireNonNull((TraineeResponseDTO) lastResponse.getBody()).getUserName();
		assertEquals(requestUserName, traineeUserName);
	}

	@When("trainee {string} with password {string} wants to find another trainee by userName {string}")
	public void authenticatedTraineeWantsToFindAnotherTraineeProfile(
		final String userName,
		final String password,
		final String requestedUserName
	) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + requestedUserName;
		final var request = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, request, String.class);
	}

	@When("unauthenticated user wants to find a trainee by userName {string}")
	public void unauthenticatedWantsToFindTraineeByUserName(final String userName) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + userName;
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.GET, null, TraineeResponseDTO.class);
	}

	@Then("trainee {string} with password {string} wants to update own trainer list")
	public void authenticatedTraineeWantsToUpdateOwnTrainerList(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE + UPDATE_TRAINERS;
		final var request = new TraineeUpdateTrainerListRequestDTO(userName, trainerNamesToBeUpdated);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, List.class);
	}

	@And("updated trainer list should be returned")
	@SuppressWarnings("unchecked")
	public void updatedTrainerListShouldBeReturned() {
		final var updatedTrainers = ((List<TraineeResponseDTO.TrainerDTO>) Objects.requireNonNull(lastResponse.getBody()));
		assertEquals(trainerNamesToBeUpdated.size(), updatedTrainers.size());
	}

	@Then("trainee {string} with password {string} wants to update another trainee {string} trainer list")
	public void authenticatedTraineeWantsToUpdateAnotherTraineeTrainerList(
		final String userName,
		final String password,
		final String requestUserName
	) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE + UPDATE_TRAINERS;
		final var request = new TraineeUpdateTrainerListRequestDTO(requestUserName, trainerNamesToBeUpdated);
		final var httpEntity = new HttpEntity<>(request, getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
	}

	@When("unauthenticated user wants to update any trainee trainer list")
	public void unauthenticatedWantsToUpdateAnyTraineeTrainerList() {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE + UPDATE_TRAINERS;
		final var request = new TraineeUpdateTrainerListRequestDTO("Any.Name", trainerNamesToBeUpdated);
		final var httpEntity = new HttpEntity<>(request);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, String.class);
	}

	@When("trainee {string} with password {string} wants to delete own profile by userName")
	public void authenticatedTraineeWantsToDeleteOwnProfileByUserName(final String userName, final String password) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + userName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
	}

	@When("trainee {string} with password {string} wants to delete another trainee by userName {string}")
	public void authenticatedTraineeWantsToDeleteAnotherTraineeByUserName(
		final String userName,
		final String password,
		final String requestedUserName
	) {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + requestedUserName;
		final var httpEntity = new HttpEntity<>(getAuthHeaders(userName, password));
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.DELETE, httpEntity, String.class);
	}

	@When("unauthenticated user wants to delete any trainee profile")
	public void unauthenticatedWantsToDeleteAnyTraineeProfile() {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE_WITH_USERNAME + "Any.Name";
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.DELETE, null, String.class);
	}

	private HttpHeaders getAuthHeaders(final String userName, final String password) {
		final var headers = new HttpHeaders();
		headers.setBasicAuth(userName, password);
		return headers;
	}

	private void updateTraineeProfile(
		final String userName,
		final String password,
		final String requestUserName,
		final String requestFirstName,
		final String requestLastName,
		final Class<?> returnType
	) throws ParseException {
		final var requestUrl = LOCALHOST_URL + port + TraineeRestController.URL + PROFILE;
		final var headers = getAuthHeaders(userName, password);
		final var trainee = getTraineeUpdateRequestDTO(requestUserName, requestFirstName, requestLastName);
		final var httpEntity = new HttpEntity<>(trainee, headers);
		lastResponse = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, returnType);
	}

	private TraineeUpdateRequestDTO getTraineeUpdateRequestDTO(
		final String userName,
		final String firstName,
		final String lastName
	) throws ParseException {
		return new TraineeUpdateRequestDTO(
			userName,
			firstName,
			lastName,
			formatter.parse("21-Sep-1986"),
			"3267 Mercer Street",
			true
		);
	}
}
