package com.epam.dmgolub.gym.converter;

import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class TrainingTypeDTOConverterTest {

	private TrainingTypeDTOConverter trainingTypeDTOConverter;

	@Mock
	private TrainingTypeService trainingTypeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		trainingTypeDTOConverter = new TrainingTypeDTOConverter();
		trainingTypeDTOConverter.setTrainingTypeService(trainingTypeService);
	}

	@Test
	void convertTest() {
		final var type = new TrainingTypeDTO();
		when(trainingTypeService.findById(1L)).thenReturn(type);

		final TrainingTypeDTO result = trainingTypeDTOConverter.convert("1");

		assertEquals(type, result);
	}

	@Test
	void convertNullTest() {
		when(trainingTypeService.findById(2L)).thenReturn(null);

		final TrainingTypeDTO result = trainingTypeDTOConverter.convert("2");

		assertNull(result);
	}
}
