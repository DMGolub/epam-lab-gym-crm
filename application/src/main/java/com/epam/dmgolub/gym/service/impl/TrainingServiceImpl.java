package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.repository.specification.TraineeTrainingSpecification;
import com.epam.dmgolub.gym.repository.specification.TrainerTrainingSpecification;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.service.TrainingService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

	private final TrainingRepository trainingRepository;
	private final EntityToModelMapper mapper;

	public TrainingServiceImpl(
		final TrainingRepository trainingRepository,
		final EntityToModelMapper mapper
	) {
		this.trainingRepository = trainingRepository;
		this.mapper = mapper;
	}

	@Override
	public TrainingModel save(final TrainingModel request) {
		LOGGER.debug("In save - Saving training from request {}", request);
		final Training training = mapper.mapToTraining(request);
		return mapper.mapToTrainingModel(trainingRepository.saveAndFlush(training));
	}

	@Override
	public List<TrainingModel> findAll() {
		LOGGER.debug("In findAll - Fetching all trainings from repository");
		return mapper.mapToTrainingModelList(trainingRepository.findAll());
	}

	@Override
	public List<TrainingModel> searchByTrainee(final TraineeTrainingsSearchRequest request) {
		LOGGER.debug("In searchByTrainee - Received search request={}", request);
		final var criteria = mapper.mapToTraineeTrainingsSearchCriteria(request);
		final List<Training> trainings = trainingRepository.findAll(new TraineeTrainingSpecification(criteria));
		LOGGER.debug("In searchByTrainee - found {} trainings", trainings.size());
		return mapper.mapToTrainingModelList(trainings);
	}

	@Override
	public List<TrainingModel> searchByTrainer(final TrainerTrainingsSearchRequest request) {
		LOGGER.debug("In searchByTrainer - Received search request={}", request);
		final var criteria = mapper.mapToTrainerTrainingsSearchCriteria(request);
		final List<Training> trainings = trainingRepository.findAll(new TrainerTrainingSpecification(criteria));
		LOGGER.debug("In searchByTrainer - found {} trainings", trainings.size());
		return mapper.mapToTrainingModelList(trainings);
	}
}
