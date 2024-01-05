package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TrainingServiceImpl implements TrainingService {

	private static final String TRAINING_NOT_FOUND_MESSAGE = "Can not find training by id=";
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
			.orElseThrow(() -> new EntityNotFoundException(TRAINING_NOT_FOUND_MESSAGE + id));
		return mapper.trainingToTrainingResponseDTO(training);
	}

	@Override
	public List<TrainingResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainings from repository");
		return mapper.trainingListToTrainingResponseDTOList(trainingRepository.findAll());
	}
}
