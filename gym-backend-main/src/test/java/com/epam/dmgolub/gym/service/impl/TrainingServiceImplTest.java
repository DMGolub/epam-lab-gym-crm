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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		when(mapper.mapToTraining(request)).thenReturn(training);
		when(trainingRepository.saveAndFlush(training)).thenReturn(training);
		final var expected = new TrainingModel();
		when(mapper.mapToTrainingModel(training)).thenReturn(expected);

		final var result = trainingService.save(request);

		assertEquals(expected, result);
		verify(mapper).mapToTraining(request);
		verify(trainingRepository).saveAndFlush(training);
		verify(mapper).mapToTrainingModel(training);
	}

	@Test
	void searchByTrainee_shouldReturnTraineeModels_whenTrainingsExist() {
		final var request =
			new TraineeTrainingsSearchRequest("Trainee", null, null, null, null);
		final var criteria =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, null, null);
		when(mapper.mapToTraineeTrainingsSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TraineeTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingModel(), new TrainingModel());
		when(mapper.mapToTrainingModelList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainee(request));
		verify(mapper, times(1)).mapToTraineeTrainingsSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TraineeTrainingSpecification(criteria));
		verify(mapper, times(1)).mapToTrainingModelList(trainings);
	}

	@Test
	void searchByTrainer_shouldReturnTrainerModels_whenTrainingsExist() {
		final var request =
			new TrainerTrainingsSearchRequest("Trainee", null, null, null);
		final var criteria =
			new TrainerTrainingsSearchCriteria("Trainee", null, null, null);
		when(mapper.mapToTrainerTrainingsSearchCriteria(request)).thenReturn(criteria);
		final var trainings = List.of(new Training(), new Training());
		when(trainingRepository.findAll(new TrainerTrainingSpecification(criteria))).thenReturn(trainings);
		final var expected = List.of(new TrainingModel(), new TrainingModel());
		when(mapper.mapToTrainingModelList(trainings)).thenReturn(expected);

		assertEquals(expected, trainingService.searchByTrainer(request));
		verify(mapper, times(1)).mapToTrainerTrainingsSearchCriteria(request);
		verify(trainingRepository, times(1)).findAll(new TrainerTrainingSpecification(criteria));
		verify(mapper, times(1)).mapToTrainingModelList(trainings);
	}
}
