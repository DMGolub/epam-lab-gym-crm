package com.epam.dmgolub.gym.converter;

import com.epam.dmgolub.gym.dto.mvc.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.mvc.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.service.TrainingTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TrainingTypeDTOConverterTest {

	private TrainingTypeDTOConverter trainingTypeDTOConverter;

	@Mock
	private TrainingTypeService trainingTypeService;
	@Mock
	private ModelToDtoMapper mapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		trainingTypeDTOConverter = new TrainingTypeDTOConverter();
		trainingTypeDTOConverter.setTrainingTypeService(trainingTypeService);
		trainingTypeDTOConverter.setModelToDtoMapper(mapper);
	}

	@Test
	void convertTest() {
		final var type = new TrainingTypeModel();
		when(trainingTypeService.findById(1L)).thenReturn(type);
		final var expected = new TrainingTypeDTO();
		when(mapper.mapToTrainingTypeDTO(type)).thenReturn(expected);

		final TrainingTypeDTO result = trainingTypeDTOConverter.convert("1");

		assertEquals(expected, result);
	}

	@Test
	void convertNullTest() {
		when(trainingTypeService.findById(2L)).thenReturn(null);

		final TrainingTypeDTO result = trainingTypeDTOConverter.convert("2");

		assertNull(result);
	}
}
