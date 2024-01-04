package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainingTypeDAO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
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
	private TrainingTypeDAO trainingTypeDAO;
	@Mock
	private MapStructMapper mapper;
	@InjectMocks
	private TrainingTypeServiceImpl trainingTypeService;

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainingTypeDTO_whenTrainingTypeExists() {
			final TrainingType trainingType = new TrainingType();
			final Long id = 1L;
			trainingType.setId(id);
			when(trainingTypeDAO.findById(id)).thenReturn(Optional.of(trainingType));
			final TrainingTypeDTO trainingTypeDTO = new TrainingTypeDTO();
			when(mapper.trainingTypeToTrainingTypeDTO(trainingType)).thenReturn(trainingTypeDTO);

			assertEquals(trainingTypeDTO, trainingTypeService.findById(id));
			verify(trainingTypeDAO, times(1)).findById(id);
			verify(mapper, times(1)).trainingTypeToTrainingTypeDTO(trainingType);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long id = 99L;
			when(trainingTypeDAO.findById(id)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainingTypeService.findById(id));
			verify(trainingTypeDAO, times(1)).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainingTypeDTOs_whenThereAreTwoTrainingTypes() {
		final List<TrainingType> trainingTypes = List.of(new TrainingType(), new TrainingType());
		when(trainingTypeDAO.findAll()).thenReturn(trainingTypes);
		final List<TrainingTypeDTO> response = List.of(new TrainingTypeDTO(), new TrainingTypeDTO());
		when(mapper.trainingTypeListToTrainingTypeDTOList(trainingTypes)).thenReturn(response);

		assertEquals(response, trainingTypeService.findAll());
		verify(trainingTypeDAO, times(1)).findAll();
		verify(mapper, times(1)).trainingTypeListToTrainingTypeDTOList(trainingTypes);
	}
}
