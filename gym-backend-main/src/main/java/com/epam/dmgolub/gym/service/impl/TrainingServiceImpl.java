package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.repository.specification.TraineeTrainingSpecification;
import com.epam.dmgolub.gym.repository.specification.TrainerTrainingSpecification;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.WorkloadUpdateService;
import jakarta.transaction.Transactional;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

	private final TrainingRepository trainingRepository;
	private final WorkloadUpdateService workloadUpdateService;
	private final EntityToModelMapper mapper;

	public TrainingServiceImpl(
		final TrainingRepository trainingRepository,
		final WorkloadUpdateService workloadUpdateService,
		final EntityToModelMapper mapper
	) {
		this.trainingRepository = trainingRepository;
		this.workloadUpdateService = workloadUpdateService;
		this.mapper = mapper;
	}

	@Override
	public TrainingModel save(final TrainingModel request) {
		LOGGER.debug("[{}] In save - Saving training from request {}", MDC.get(TRANSACTION_ID), request);
		final var training = mapper.mapToTrainingModel(trainingRepository.saveAndFlush(mapper.mapToTraining(request)));
		workloadUpdateService.add(training);
		return training;
	}

	@Override
	public List<TrainingModel> searchByTrainee(final TraineeTrainingsSearchRequest request) {
		LOGGER.debug("[{}] In searchByTrainee - Received search request={}", MDC.get(TRANSACTION_ID), request);
		final var criteria = mapper.mapToTraineeTrainingsSearchCriteria(request);
		final var trainings = trainingRepository.findAll(new TraineeTrainingSpecification(criteria));
		LOGGER.debug("[{}] In searchByTrainee - found {} trainings", MDC.get(TRANSACTION_ID), trainings.size());
		return mapper.mapToTrainingModelList(trainings);
	}

	@Override
	public List<TrainingModel> searchByTrainer(final TrainerTrainingsSearchRequest request) {
		LOGGER.debug("[{}] In searchByTrainer - Received search request={}", MDC.get(TRANSACTION_ID), request);
		final var criteria = mapper.mapToTrainerTrainingsSearchCriteria(request);
		final var trainings = trainingRepository.findAll(new TrainerTrainingSpecification(criteria));
		LOGGER.debug("[{}] In searchByTrainer - found {} trainings", MDC.get(TRANSACTION_ID), trainings.size());
		return mapper.mapToTrainingModelList(trainings);
	}

	@Override
	public List<TrainingModel> findAll() {
		LOGGER.debug("[{}] In findAll - Received request to find all trainings", MDC.get(TRANSACTION_ID));
		final var trainings = mapper.mapToTrainingModelList(trainingRepository.findAll());
		LOGGER.debug("[{}] In findAll - Found {} trainings", MDC.get(TRANSACTION_ID), trainings.size());
		return trainings;
	}

	@Override
	public void delete(final TrainingModel training) {
		LOGGER.debug("[{}] In delete - Received request to delete training {}", MDC.get(TRANSACTION_ID), training);
		trainingRepository.delete(mapper.mapToTraining(training));
		workloadUpdateService.delete(training);
	}
}
