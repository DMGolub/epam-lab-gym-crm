package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.WorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.DtoToModelMapper;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import com.epam.dmgolub.gym.service.WorkloadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerWorkloadRestControllerTest {

	private static final String URL = TrainerWorkloadRestController.URL;

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final String userName = "FirstName.LastName";
	private final String firstName = "FirstName";
	private final String lastName = "LastName";
	private final boolean isActive = true;
	private final int duration = 90;
	@Mock
	private WorkloadService workloadService;
	@Mock
	private DtoToModelMapper mapper;
	@InjectMocks
	private TrainerWorkloadRestController trainerWorkloadRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(trainerWorkloadRestController).build();
	}

	@Test
	void update_shouldInvokeAddTraining_whenActionTypeIsAdd() throws Exception {
		final var date = dateFormat.parse("2025-04-05");
		final var requestDTO =
			new WorkloadUpdateRequestDTO(userName, firstName, lastName, isActive, date, duration, "ADD");
		final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
		when(mapper.mapToWorkloadUpdateRequest(requestDTO)).thenReturn(request);

		mockMvc.perform(post(URL + "/update").contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk());

		verify(mapper).mapToWorkloadUpdateRequest(requestDTO);
		verify(workloadService).addWorkload(request);
		verifyNoMoreInteractions(workloadService);
	}

	@Test
	void update_shouldInvokeDeleteTraining_whenActionTypeIsAdd() throws Exception {
		final var date = dateFormat.parse("2025-04-05");
		final var requestDTO =
			new WorkloadUpdateRequestDTO(userName, firstName, lastName, isActive, date, duration, "DELETE");
		final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
		when(mapper.mapToWorkloadUpdateRequest(requestDTO)).thenReturn(request);

		mockMvc.perform(post(URL + "/update").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(requestDTO)))
			.andExpect(status().isOk());

		verify(mapper).mapToWorkloadUpdateRequest(requestDTO);
		verify(workloadService).deleteWorkload(request);
		verifyNoMoreInteractions(workloadService);
	}
}
