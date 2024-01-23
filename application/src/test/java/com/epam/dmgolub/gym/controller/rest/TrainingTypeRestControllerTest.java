package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.dto.rest.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingTypeRestControllerTest {

	private static final String URL = TrainingTypeRestController.URL;

	@Mock
	private TrainingTypeService trainingTypeService;
	@Mock
	private ModelToRestDtoMapper mapper;
	@InjectMocks
	private TrainingTypeRestController trainingTypeRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(trainingTypeRestController).build();
	}

	@Test
	void getAll_shouldReturnOk_whenServiceReturnsTrainingTypes() throws Exception {
		final var trainingTypes = List.of(new TrainingTypeModel(), new TrainingTypeModel());
		when(trainingTypeService.findAll()).thenReturn(trainingTypes);
		final var response = List.of(new TrainingTypeDTO(), new TrainingTypeDTO());
		when(mapper.mapToTrainingTypeDTOList(trainingTypes)).thenReturn(response);

		mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(response.size())));

		verify(trainingTypeService, times(1)).findAll();
		verify(mapper, times(1)).mapToTrainingTypeDTOList(trainingTypes);
	}

	@Test
	void getById_shouldReturnOn_whenTrainingTypeExists() throws Exception {
		final Long id = 1L;
		final var trainingType = new TrainingTypeModel();
		when(trainingTypeService.findById(id)).thenReturn(trainingType);
		final var response = new TrainingTypeDTO();
		when(mapper.mapToTrainingTypeDTO(trainingType)).thenReturn(response);

		mockMvc.perform(get(URL + "/" + id).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		verify(trainingTypeService, times(1)).findById(id);
		verify(mapper, times(1)).mapToTrainingTypeDTO(trainingType);
	}
}
