package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.repository.specification.TraineeTrainingSpecification;
import com.epam.dmgolub.gym.repository.specification.TrainerTrainingSpecification;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.TRAINING_NOT_FOUND_BY_ID_MESSAGE;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

	private final TrainingRepository trainingRepository;
	private final MapStructMapper mapper;

	public TrainingServiceImpl(
		final TrainingRepository trainingRepository,
		final MapStructMapper mapper
	) {
		this.trainingRepository = trainingRepository;
		this.mapper = mapper;
	}

	@Override
	public TrainingResponseDTO save(final TrainingRequestDTO request) {
		LOGGER.debug("In save - Saving training from request {}", request);
		final Training training = mapper.trainingRequestDTOToTraining(request);
		return mapper.trainingToTrainingResponseDTO(trainingRepository.saveAndFlush(training));
	}

	@Override
	public TrainingResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching training by id={} from repository", id);
		final var training = trainingRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINING_NOT_FOUND_BY_ID_MESSAGE + id));
		return mapper.trainingToTrainingResponseDTO(training);
	}

	@Override
	public List<TrainingResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainings from repository");
		return mapper.trainingListToTrainingResponseDTOList(trainingRepository.findAll());
	}

	@Override
	public List<TrainingResponseDTO> searchByTrainee(TraineeTrainingsSearchRequestDTO request) {
		LOGGER.debug("In searchByTrainee - Received search request={}", request);
		final var criteria = mapper.searchRequestToSearchCriteria(request);
		final List<Training> trainings = trainingRepository.findAll(new TraineeTrainingSpecification(criteria));
		LOGGER.debug("In searchByTrainee - found {} trainings", trainings.size());
		return mapper.trainingListToTrainingResponseDTOList(trainings);
	}

	@Override
	public List<TrainingResponseDTO> searchByTrainer(TrainerTrainingsSearchRequestDTO request) {
		LOGGER.debug("In searchByTrainer - Received search request={}", request);
		final var criteria = mapper.searchRequestToSearchCriteria(request);
		final List<Training> trainings = trainingRepository.findAll(new TrainerTrainingSpecification(criteria));
		LOGGER.debug("In searchByTrainer - found {} trainings", trainings.size());
		return mapper.trainingListToTrainingResponseDTOList(trainings);
	}
}
