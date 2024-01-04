package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainingTypeDAO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

	private static final String TRAINING_TYPE_NOT_FOUND_MESSAGE = "Can not find training type by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

	@Autowired
	private TrainingTypeDAO trainingTypeDAO;
	private MapStructMapper mapper;

	@Autowired
	public void setMapper(final MapStructMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public TrainingTypeDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching training type by id={} from DAO", id);
		final var trainingType = trainingTypeDAO.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_MESSAGE + id));
		return mapper.trainingTypeToTrainingTypeDTO(trainingType);
	}

	@Override
	public List<TrainingTypeDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all training types from DAO");
		return mapper.trainingTypeListToTrainingTypeDTOList(trainingTypeDAO.findAll());
	}
}
