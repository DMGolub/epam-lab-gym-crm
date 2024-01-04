package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainingDAO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

	private static final String TRAINING_NOT_FOUND_MESSAGE = "Can not find training by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

	@Autowired
	private TrainingDAO trainingDAO;
	private MapStructMapper mapper;

	@Autowired
	public void setMapper(final MapStructMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public TrainingResponseDTO save(final TrainingRequestDTO request) {
		LOGGER.debug("In save - Saving training from request {}", request);
		final Training training = mapper.trainingRequestDTOToTraining(request);
		return mapper.trainingToTrainingResponseDTO(trainingDAO.save(training));
	}

	@Override
	public TrainingResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching training by id={} from DAO", id);
		final var training = trainingDAO.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINING_NOT_FOUND_MESSAGE + id));
		return mapper.trainingToTrainingResponseDTO(training);
	}

	@Override
	public List<TrainingResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainings from DAO");
		return mapper.trainingListToTrainingResponseDTOList(trainingDAO.findAll());
	}
}
