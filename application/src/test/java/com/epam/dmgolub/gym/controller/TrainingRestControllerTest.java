package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.service.TrainingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingRestControllerTest {

	private static final String URL = TrainingRestController.URL;

	@Mock
	private TrainingService trainingService;
	@Mock
	private ModelToRestDtoMapper mapper;
	@Mock
	private UserDetails userDetails;
	@InjectMocks
	private TrainingRestController trainingRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(trainingRestController).build();
		final Authentication authentication = mock(Authentication.class);
		final SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
	}

	@Test
	void searchByTrainee_shouldReturnOk_whenServiceReturnsTrainings() throws Exception {
		final String userName = "User.Name";
		final var request = new TraineeTrainingsSearchRequest(userName, null, null, null, null);
		final var trainings = List.of(new TrainingModel(), new TrainingModel());
		when(trainingService.searchByTrainee(request)).thenReturn(trainings);
		final var response = List.of(new TraineeTrainingResponseDTO(), new TraineeTrainingResponseDTO());
		when(mapper.mapToTraineeTrainingResponseDTOList(trainings)).thenReturn(response);
		when(userDetails.getUsername()).thenReturn(userName);

		mockMvc.perform(get(URL + "/search-by-trainee").param("traineeUserName", userName))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(trainingService, times(1)).searchByTrainee(request);
		verify(mapper, times(1)).mapToTraineeTrainingResponseDTOList(trainings);
	}

	@Test
	void searchByTrainer_shouldReturnOk_whenServiceReturnsTrainings() throws Exception {
		final String userName = "User.Name";
		final var request = new TrainerTrainingsSearchRequest(userName, null, null, null);
		final var trainings = List.of(new TrainingModel(), new TrainingModel());
		when(trainingService.searchByTrainer(request)).thenReturn(trainings);
		final var response = List.of(new TrainerTrainingResponseDTO(), new TrainerTrainingResponseDTO());
		when(mapper.mapToTrainerTrainingResponseDTOList(trainings)).thenReturn(response);
		when(userDetails.getUsername()).thenReturn(userName);

		mockMvc.perform(get(URL + "/search-by-trainer").param("trainerUserName", userName))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(trainingService, times(1)).searchByTrainer(request);
		verify(mapper, times(1)).mapToTrainerTrainingResponseDTOList(trainings);
	}

	@Test
	void create_shouldReturnOk_whenTrainingCreatedSuccessfully() throws Exception {
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		final Date date = formatter.parse("7-Jun-2025");
		final String traineeUserName = "User.Name2";
		final String trainerUserName = "User.Name3";
		final int duration = 60;
		final var requestDTO = new TrainingCreateRequestDTO(traineeUserName, trainerUserName, "Name", date, duration);
		final var trainee = new TraineeModel();
		trainee.setUserName(traineeUserName);
		final var trainer = new TrainerModel();
		trainer.setUserName(trainerUserName);
		final var type = new TrainingTypeModel(1L, "Bodybuilding");
		trainer.setSpecialization(type);
		final var request = new TrainingModel(null, trainee, trainer, "Name", type, date, duration);
		when(mapper.mapToTrainingModel(requestDTO)).thenReturn(request);
		when(trainingService.save(request)).thenReturn(request);
		when(userDetails.getUsername()).thenReturn(traineeUserName);

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk());

		verify(mapper, times(1)).mapToTrainingModel(requestDTO);
		verify(trainingService, times(1)).save(request);
	}
}
