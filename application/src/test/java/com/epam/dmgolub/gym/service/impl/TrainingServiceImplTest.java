package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.searchcriteria.TraineeTrainingsSearchCriteria;
import com.epam.dmgolub.gym.repository.searchcriteria.TrainerTrainingsSearchCriteria;
import com.epam.dmgolub.gym.repository.specification.TraineeTrainingSpecification;
import com.epam.dmgolub.gym.repository.specification.TrainerTrainingSpecification;
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
	private TrainingRepository trainingRepository;
	@Mock
	private MapStructMapper mapper;
	@InjectMocks
	private TrainingServiceImpl trainingService;

	@Test
	void save_shouldReturnTrainingResponseDTO_whenInvoked() {
		final TrainingRequestDTO request = new TrainingRequestDTO();
		final Training training = new Training();
		when(mapper.trainingRequestDTOToTraining(request)).thenReturn(training);
		when(trainingRepository.saveAndFlush(training)).thenReturn(training);
		final TrainingResponseDTO expected = new TrainingResponseDTO();
		when(mapper.trainingToTrainingResponseDTO(training)).thenReturn(expected);

		final TrainingResponseDTO result = trainingService.save(request);

		assertEquals(expected, result);
		verify(mapper).trainingRequestDTOToTraining(request);
		verify(trainingRepository).saveAndFlush(training);
		verify(mapper).trainingToTrainingResponseDTO(training);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainingResponseDTO_whenTrainingExists() {
			final Training training = new Training();
			final Long id = 1L;
			training.setId(id);
			when(trainingRepository.findById(id)).thenReturn(Optional.of(training));
			final TrainingResponseDTO expectedResponse = new TrainingResponseDTO();
			when(mapper.trainingToTrainingResponseDTO(training)).thenReturn(expectedResponse);

			final TrainingResponseDTO response = trainingService.findById(id);
			assertEquals(expectedResponse, response);
			verify(trainingRepository).findById(id);
			verify(mapper).trainingToTrainingResponseDTO(training);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainingNotFound() {
			final Long id = 99L;
			when(trainingRepository.findById(id)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainingService.findById(id));
			verify(trainingRepository).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainingResponseDTOs_whenThereAreTwoTrainings() {
		final List<Training> trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll()).thenReturn(trainings);
		final List<TrainingResponseDTO> response = List.of(new TrainingResponseDTO(), new TrainingResponseDTO());
		when(mapper.trainingListToTrainingResponseDTOList(trainings)).thenReturn(response);

		assertEquals(response, trainingService.findAll());
		verify(trainingRepository).findAll();
		verify(mapper).trainingListToTrainingResponseDTOList(trainings);
	}

	@Test
	void searchByTrainee_shouldReturnTraineeResponseDTOs_whenTrainingsExist() {
		final var request =
			new TraineeTrainingsSearchRequestDTO("Trainee", null, null, null, null);
		final var criteria =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, null, null);
		when(mapper.searchRequestToSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TraineeTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingResponseDTO(), new TrainingResponseDTO());
		when(mapper.trainingListToTrainingResponseDTOList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainee(request));
		verify(mapper, times(1)).searchRequestToSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TraineeTrainingSpecification(criteria));
		verify(mapper, times(1)).trainingListToTrainingResponseDTOList(trainings);
	}

	@Test
	void searchByTrainer_shouldReturnTrainerResponseDTOs_whenTrainingsExist() {
		final var request =
			new TrainerTrainingsSearchRequestDTO("Trainee", null, null, null);
		final var criteria =
			new TrainerTrainingsSearchCriteria("Trainee", null, null, null);
		when(mapper.searchRequestToSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TrainerTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingResponseDTO(), new TrainingResponseDTO());
		when(mapper.trainingListToTrainingResponseDTOList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainer(request));
		verify(mapper, times(1)).searchRequestToSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TrainerTrainingSpecification(criteria));
		verify(mapper, times(1)).trainingListToTrainingResponseDTOList(trainings);
	}
}
