package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

	@Mock
	private TrainingTypeRepository trainingTypeRepository;
	@Mock
	private EntityToModelMapper mapper;
	@InjectMocks
	private TrainingTypeServiceImpl trainingTypeService;

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainingTypeDTO_whenTrainingTypeExists() {
			final var trainingType = new TrainingType();
			final Long id = 1L;
			trainingType.setId(id);
			when(trainingTypeRepository.findById(id)).thenReturn(Optional.of(trainingType));
			final var trainingTypeModel = new TrainingTypeModel();
			when(mapper.mapToTrainingTypeModel(trainingType)).thenReturn(trainingTypeModel);

			assertEquals(trainingTypeModel, trainingTypeService.findById(id));
			verify(trainingTypeRepository, times(1)).findById(id);
			verify(mapper, times(1)).mapToTrainingTypeModel(trainingType);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long id = 99L;
			when(trainingTypeRepository.findById(id)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainingTypeService.findById(id));
			verify(trainingTypeRepository, times(1)).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainingTypeDTOs_whenThereAreTwoTrainingTypes() {
		final List<TrainingType> trainingTypes = List.of(new TrainingType(), new TrainingType());
		when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);
		final List<TrainingTypeModel> response = List.of(new TrainingTypeModel(), new TrainingTypeModel());
		when(mapper.mapToTrainingTypeModelList(trainingTypes)).thenReturn(response);

		assertEquals(response, trainingTypeService.findAll());
		verify(trainingTypeRepository, times(1)).findAll();
		verify(mapper, times(1)).mapToTrainingTypeModelList(trainingTypes);
	}
}
