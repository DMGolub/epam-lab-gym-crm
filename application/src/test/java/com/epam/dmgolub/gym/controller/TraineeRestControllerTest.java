package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.service.TraineeService;
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
import java.util.ArrayList;
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

class TraineeRestControllerTest {

	private static final String URL = TraineeRestController.URL;

	@Mock
	private TraineeService traineeService;
	@Mock
	private ModelToRestDtoMapper mapper;
	@InjectMocks
	private TraineeRestController traineeRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(traineeRestController).build();
	}

	@Test
	void getAll_shouldReturnOk_whenServiceReturnsTrainees() throws Exception {
		final var trainees = List.of(new TraineeModel(), new TraineeModel());
		when(traineeService.findAll()).thenReturn(trainees);
		final var response = List.of(new TraineeResponseDTO(), new TraineeResponseDTO());
		when(mapper.mapToTraineeResponseDTOList(trainees)).thenReturn(response);

		mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(traineeService, times(1)).findAll();
		verify(mapper, times(1)).mapToTraineeResponseDTOList(trainees);
	}

	@Test
	void getByUserName_shouldReturnOk_whenTraineeExists() throws Exception {
		final String userName = "User.Name";
		final var trainee = new TraineeModel();
		when(traineeService.findByUserName(userName)).thenReturn(trainee);
		final var response = new TraineeResponseDTO();
		when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(response);

		mockMvc.perform(get(URL + "/profile")
				.contentType(MediaType.APPLICATION_JSON)
				.param("userName", userName))
			.andExpect(status().isOk());

		verify(traineeService, times(1)).findByUserName(userName);
		verify(mapper, times(1)).mapToTraineeResponseDTO(trainee);
	}

	@Test
	void create_shouldReturnCredentials_whenTraineeCreatedSuccessfully() throws Exception {
		final String firstName = "User";
		final String lastName = "Name";
		final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		final Date date = formatter.parse("7-Jun-2005");
		final String address = "Address";
		final var requestDTO = new TraineeCreateRequestDTO(firstName, lastName, date, address);
		final var request =
			new TraineeModel(null, firstName, lastName, null, null, false, null, date, address, new ArrayList<>());
		when(mapper.mapToTraineeModel(requestDTO)).thenReturn(request);
		final var credentials = new Credentials("User.Name", "Password");
		when(traineeService.save(request)).thenReturn(credentials);

		mockMvc.perform(post(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isCreated());

		verify(mapper, times(1)).mapToTraineeModel(requestDTO);
		verify(mapper, times(1)).mapToCredentialsDTO(credentials);
		verify(traineeService, times(1)).save(request);
	}
}
