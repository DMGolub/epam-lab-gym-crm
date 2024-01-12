package com.epam.dmgolub.gym.converter;

import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeDTOConverter implements Converter<String, TrainingTypeDTO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeDTOConverter.class);

	private TrainingTypeService trainingTypeService;
	private ModelToDtoMapper modelToDtoMapper;

	@Autowired
	public void setTrainingTypeService(final TrainingTypeService trainingTypeService) {
		this.trainingTypeService = trainingTypeService;
	}

	@Autowired
	public void setModelToDtoMapper(final ModelToDtoMapper modelToDtoMapper) {
		this.modelToDtoMapper = modelToDtoMapper;
	}

	@Override
	public TrainingTypeDTO convert(@NonNull final String id) {
		LOGGER.debug("In convert - Fetching training type by id={} from service", id);
		final var type = trainingTypeService.findById(Long.valueOf(id));
		return modelToDtoMapper.trainingTypeToTrainingTypeDTO(type);
	}
}
