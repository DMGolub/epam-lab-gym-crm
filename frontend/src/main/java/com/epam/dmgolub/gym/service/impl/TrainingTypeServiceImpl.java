package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeServiceImpl.class);

	private final RestTemplate restTemplate;

	@Value("${backend.url}")
	private String backendUrl;

	public TrainingTypeServiceImpl(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public List<TrainingTypeDTO> findAll() {
		LOGGER.debug("In findAll - Fetching training types from backend");
		final String requestUrl = backendUrl + "/api/v1/training-types";
		final var trainingTypes = restTemplate.getForObject(requestUrl, TrainingTypeDTO[].class);
		List<TrainingTypeDTO> result = new ArrayList<>();
		if (trainingTypes != null) {
			result = Arrays.asList(trainingTypes);
		}
		return result;
	}

	@Override
	public TrainingTypeDTO findById(final Long id) {
		LOGGER.debug("In findByLink - Fetching training type by id={}", id);
		final String requestUrl = backendUrl + "/api/v1/training-types/" + id;
		return getTrainingType(requestUrl)
			.orElseThrow(() -> new EntityNotFoundException("Can not find training type by id=" + id));
	}

	@Override
	public TrainingTypeDTO findByLink(final String link) {
		LOGGER.debug("In findByLink - Fetching training type by link={}", link);
		final String requestUrl = backendUrl + link;
		return getTrainingType(requestUrl)
			.orElseThrow(() -> new EntityNotFoundException("Can not find training type by link=" + link));
	}

	private Optional<TrainingTypeDTO> getTrainingType(final String requestUrl) {
		final var trainingType = restTemplate.getForObject(requestUrl, TrainingTypeDTO.class);
		if (trainingType != null) {
			return Optional.of(trainingType);
		}
		return Optional.empty();
	}
}
