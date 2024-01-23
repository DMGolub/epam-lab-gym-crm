package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.dto.rest.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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
	@InjectMocks
	private TrainingRestController trainingRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(trainingRestController).build();
	}

	@Test
	void searchByTrainee_shouldReturnOk_whenServiceReturnsTrainings() throws Exception {
		final String userName = "User.Name";
		final var requestDTO = new TraineeTrainingsSearchRequestDTO(userName, null, null, null, null);
		final var request = new TraineeTrainingsSearchRequest(userName, null, null, null, null);
		when(mapper.mapToTraineeTrainingsSearchRequest(requestDTO)).thenReturn(request);
		final var trainings = List.of(new TrainingModel(), new TrainingModel());
		when(trainingService.searchByTrainee(request)).thenReturn(trainings);
		final var response = List.of(new TraineeTrainingResponseDTO(), new TraineeTrainingResponseDTO());
		when(mapper.mapToTraineeTrainingResponseDTOList(trainings)).thenReturn(response);

		mockMvc.perform(get(URL + "/search-by-trainee")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(mapper, times(1)).mapToTraineeTrainingsSearchRequest(requestDTO);
		verify(trainingService, times(1)).searchByTrainee(request);
		verify(mapper, times(1)).mapToTraineeTrainingResponseDTOList(trainings);
	}

	@Test
	void searchByTrainer_shouldReturnOk_whenServiceReturnsTrainings() throws Exception {
		final String userName = "User.Name";
		final var requestDTO = new TrainerTrainingsSearchRequestDTO(userName, null, null, null);
		final var request = new TrainerTrainingsSearchRequest(userName, null, null, null);
		when(mapper.mapToTrainerTrainingsSearchRequest(requestDTO)).thenReturn(request);
		final var trainings = List.of(new TrainingModel(), new TrainingModel());
		when(trainingService.searchByTrainer(request)).thenReturn(trainings);
		final var response = List.of(new TrainerTrainingResponseDTO(), new TrainerTrainingResponseDTO());
		when(mapper.mapToTrainerTrainingResponseDTOList(trainings)).thenReturn(response);

		mockMvc.perform(get(URL + "/search-by-trainer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(mapper, times(1)).mapToTrainerTrainingsSearchRequest(requestDTO);
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

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk());

		verify(mapper, times(1)).mapToTrainingModel(requestDTO);
		verify(trainingService, times(1)).save(request);
	}
}
