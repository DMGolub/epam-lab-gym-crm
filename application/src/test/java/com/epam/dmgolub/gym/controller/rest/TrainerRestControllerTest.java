package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.dto.rest.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerResponseDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.service.TrainerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerRestControllerTest {

	private static final String URL = TrainerRestController.URL;

	@Mock
	private TrainerService trainerService;
	@Mock
	private ModelToRestDtoMapper mapper;
	@InjectMocks
	private TrainerRestController trainerRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(trainerRestController).build();
	}

	@Test
	void getAll_shouldReturnOk_whenServiceReturnsTrainers() throws Exception {
		final var trainers = List.of(new TrainerModel(), new TrainerModel());
		when(trainerService.findAll()).thenReturn(trainers);
		final var response = List.of(new TrainerResponseDTO(), new TrainerResponseDTO());
		when(mapper.mapToTrainerResponseDTOList(trainers)).thenReturn(response);

		mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(trainerService, times(1)).findAll();
		verify(mapper, times(1)).mapToTrainerResponseDTOList(trainers);
	}

	@Test
	void getNotAssignedOnTrainee_shouldReturnOk_whenServiceReturnsTrainers() throws Exception {
		final String userName = "User.Name";
		final List<TrainerModel> trainers = List.of(new TrainerModel(), new TrainerModel());
		when(trainerService.findActiveTrainersNotAssignedOnTrainee(userName)).thenReturn(trainers);
		final List<TraineeResponseDTO.TrainerDTO> response =
			List.of(new TraineeResponseDTO.TrainerDTO(), new TraineeResponseDTO.TrainerDTO());
		when(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers)).thenReturn(response);

		mockMvc.perform(get(URL + "/not-assigned-on")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userName", userName))
			.andExpect(status().isOk());

		verify(trainerService, times(1)).findActiveTrainersNotAssignedOnTrainee(userName);
		verify(mapper, times(1)).mapToTraineeResponseDTOTrainerDTOList(trainers);
	}

	@Test
	void getAssignedOnTrainee_shouldReturnOk_whenServiceReturnsTrainers() throws Exception {
		final String userName = "User.Name";
		final List<TrainerModel> trainers = List.of(new TrainerModel(), new TrainerModel());
		when(trainerService.findActiveTrainersAssignedOnTrainee(userName)).thenReturn(trainers);
		final List<TraineeResponseDTO.TrainerDTO> response =
			List.of(new TraineeResponseDTO.TrainerDTO(), new TraineeResponseDTO.TrainerDTO());
		when(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers)).thenReturn(response);

		mockMvc.perform(get(URL + "/assigned-on")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userName", userName))
			.andExpect(status().isOk());

		verify(trainerService, times(1)).findActiveTrainersAssignedOnTrainee(userName);
		verify(mapper, times(1)).mapToTraineeResponseDTOTrainerDTOList(trainers);
	}

	@Test
	void getByUserName_shouldReturnOk_whenTrainerExists() throws Exception {
		final String userName = "User.Name";
		final var trainer = new TrainerModel();
		when(trainerService.findByUserName(userName)).thenReturn(trainer);
		final var response = new TrainerResponseDTO();
		when(mapper.mapToTrainerResponseDTO(trainer)).thenReturn(response);

		mockMvc.perform(get(URL + "/profile")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userName", userName))
			.andExpect(status().isOk());

		verify(trainerService, times(1)).findByUserName(userName);
		verify(mapper, times(1)).mapToTrainerResponseDTO(trainer);
	}

	@Test
	void create_shouldReturnCreated_whenTrainerCreatedSuccessfully() throws Exception {
		final String firstName = "User";
		final String lastName = "Name";
		final String specialization = "/api/v1/training-types/1";
		final var requestDTO = new TrainerCreateRequestDTO(firstName, lastName, specialization);
		final var type = new TrainingTypeModel(1L, "Bodybuilding");
		final var request = new TrainerModel(null, firstName, lastName, null, null, false, null, type);
		when(mapper.mapToTrainerModel(requestDTO)).thenReturn(request);
		when(trainerService.save(request)).thenReturn(request);

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isCreated());

		verify(mapper, times(1)).mapToTrainerModel(requestDTO);
		verify(trainerService, times(1)).save(request);
	}
}
