package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
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
	private EntityToModelMapper mapper;
	@InjectMocks
	private TrainingServiceImpl trainingService;

	@Test
	void save_shouldReturnTrainingModel_whenInvoked() {
		final var request = new TrainingModel();
		final Training training = new Training();
		when(mapper.trainingModelToTraining(request)).thenReturn(training);
		when(trainingRepository.saveAndFlush(training)).thenReturn(training);
		final var expected = new TrainingModel();
		when(mapper.trainingToTrainingModel(training)).thenReturn(expected);

		final var result = trainingService.save(request);

		assertEquals(expected, result);
		verify(mapper).trainingModelToTraining(request);
		verify(trainingRepository).saveAndFlush(training);
		verify(mapper).trainingToTrainingModel(training);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainingModel_whenTrainingExists() {
			final Training training = new Training();
			final Long id = 1L;
			training.setId(id);
			when(trainingRepository.findById(id)).thenReturn(Optional.of(training));
			final TrainingModel expectedResponse = new TrainingModel();
			when(mapper.trainingToTrainingModel(training)).thenReturn(expectedResponse);

			final TrainingModel response = trainingService.findById(id);
			assertEquals(expectedResponse, response);
			verify(trainingRepository).findById(id);
			verify(mapper).trainingToTrainingModel(training);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainingNotFound() {
			final Long id = 99L;
			when(trainingRepository.findById(id)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainingService.findById(id));
			verify(trainingRepository).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainingModels_whenThereAreTwoTrainings() {
		final List<Training> trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll()).thenReturn(trainings);
		final List<TrainingModel> response = List.of(new TrainingModel(), new TrainingModel());
		when(mapper.trainingListToTrainingModelList(trainings)).thenReturn(response);

		assertEquals(response, trainingService.findAll());
		verify(trainingRepository).findAll();
		verify(mapper).trainingListToTrainingModelList(trainings);
	}

	@Test
	void searchByTrainee_shouldReturnTraineeModels_whenTrainingsExist() {
		final var request =
			new TraineeTrainingsSearchRequest("Trainee", null, null, null, null);
		final var criteria =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, null, null);
		when(mapper.searchRequestToSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TraineeTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingModel(), new TrainingModel());
		when(mapper.trainingListToTrainingModelList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainee(request));
		verify(mapper, times(1)).searchRequestToSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TraineeTrainingSpecification(criteria));
		verify(mapper, times(1)).trainingListToTrainingModelList(trainings);
	}

	@Test
	void searchByTrainer_shouldReturnTrainerModels_whenTrainingsExist() {
		final var request =
			new TrainerTrainingsSearchRequest("Trainee", null, null, null);
		final var criteria =
			new TrainerTrainingsSearchCriteria("Trainee", null, null, null);
		when(mapper.searchRequestToSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TrainerTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingModel(), new TrainingModel());
		when(mapper.trainingListToTrainingModelList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainer(request));
		verify(mapper, times(1)).searchRequestToSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TrainerTrainingSpecification(criteria));
		verify(mapper, times(1)).trainingListToTrainingModelList(trainings);
	}
}
