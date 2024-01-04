package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainingDAO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

	@Mock
	private TrainingDAO trainingDAO;
	@Mock
	private MapStructMapper mapper;
	@InjectMocks
	private TrainingServiceImpl trainingService;

	@Test
	void save_shouldReturnTrainingResponseDTO_whenInvoked() {
		final TrainingRequestDTO request = new TrainingRequestDTO();
		final Training training = new Training();
		when(mapper.trainingRequestDTOToTraining(request)).thenReturn(training);
		when(trainingDAO.save(training)).thenReturn(training);
		final TrainingResponseDTO expected = new TrainingResponseDTO();
		when(mapper.trainingToTrainingResponseDTO(training)).thenReturn(expected);

		final TrainingResponseDTO result = trainingService.save(request);

		assertEquals(expected, result);
		verify(mapper).trainingRequestDTOToTraining(request);
		verify(trainingDAO).save(training);
		verify(mapper).trainingToTrainingResponseDTO(training);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainingResponseDTO_whenTrainingExists() {
			final Training training = new Training();
			final Long id = 1L;
			training.setId(id);
			when(trainingDAO.findById(id)).thenReturn(Optional.of(training));
			final TrainingResponseDTO expectedResponse = new TrainingResponseDTO();
			when(mapper.trainingToTrainingResponseDTO(training)).thenReturn(expectedResponse);

			final TrainingResponseDTO response = trainingService.findById(id);
			assertEquals(expectedResponse, response);
			verify(trainingDAO).findById(id);
			verify(mapper).trainingToTrainingResponseDTO(training);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainingNotFound() {
			final Long id = 99L;
			when(trainingDAO.findById(id)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainingService.findById(id));
			verify(trainingDAO).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainingResponseDTOs_whenThereAreTwoTrainings() {
		final List<Training> trainings = List.of(new Training(), new Training());
		when(trainingDAO.findAll()).thenReturn(trainings);
		final List<TrainingResponseDTO> response = List.of(new TrainingResponseDTO(), new TrainingResponseDTO());
		when(mapper.trainingListToTrainingResponseDTOList(trainings)).thenReturn(response);

		assertEquals(response, trainingService.findAll());
		verify(trainingDAO).findAll();
		verify(mapper).trainingListToTrainingResponseDTOList(trainings);
	}
}
