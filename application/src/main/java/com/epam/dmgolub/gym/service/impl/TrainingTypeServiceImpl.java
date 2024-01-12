package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TrainingTypeServiceImpl implements TrainingTypeService {

	private static final String TRAINING_TYPE_NOT_FOUND_MESSAGE = "Can not find training type by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

	private final TrainingTypeRepository trainingTypeRepository;
	private final EntityToModelMapper mapper;

	public TrainingTypeServiceImpl(
		final TrainingTypeRepository trainingTypeRepository,
		final EntityToModelMapper mapper
	) {
		this.trainingTypeRepository = trainingTypeRepository;
		this.mapper = mapper;
	}

	@Override
	public TrainingTypeModel findById(final Long id) {
		LOGGER.debug("In findById - Fetching training type by id={} from repository", id);
		final var trainingType = trainingTypeRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_MESSAGE + id));
		return mapper.trainingTypeToTrainingTypeModel(trainingType);
	}

	@Override
	public List<TrainingTypeModel> findAll() {
		LOGGER.debug("In findAll - Fetching all training types from repository");
		return mapper.trainingTypeListToTrainingTypeModelList(trainingTypeRepository.findAll());
	}
}
