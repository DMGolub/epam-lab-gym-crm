package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.service.WorkloadUpdateService;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static com.epam.dmgolub.gym.service.constant.Constants.ADD_WORKLOAD_ACTION_TYPE;
import static com.epam.dmgolub.gym.service.constant.Constants.DELETE_WORKLOAD_ACTION_TYPE;

@Service
public class RestWorkloadUpdateServiceImpl implements WorkloadUpdateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestWorkloadUpdateServiceImpl.class);

	private final RestTemplate restTemplate;
	private final CircuitBreaker circuitBreaker;

	@Value("${trainer-workload-service.url}")
	private String trainerWorkloadServiceUrl;

	public RestWorkloadUpdateServiceImpl(final RestTemplate restTemplate, final CircuitBreaker circuitBreaker) {
		this.restTemplate = restTemplate;
		this.circuitBreaker = circuitBreaker;
	}

	@Override
	public void add(final TrainingModel training) {
		LOGGER.debug("[{}] In add - Received request to add training to workload {}",
			MDC.get(TRANSACTION_ID), training);
		final var response = performRequest(createRequestDTO(training , ADD_WORKLOAD_ACTION_TYPE));
		LOGGER.debug("[{}] In add - Received response from workload service: {}", MDC.get(TRANSACTION_ID), response);
	}

	@Override
	public void delete(final TrainingModel training) {
		LOGGER.debug("[{}] In delete - Received request to delete training from workload {}",
			MDC.get(TRANSACTION_ID), training);
		final var response = performRequest(createRequestDTO(training , DELETE_WORKLOAD_ACTION_TYPE));
		LOGGER.debug("[{}] In delete - Received response from workload service: {}", MDC.get(TRANSACTION_ID), response);
	}

	private TrainerWorkloadUpdateRequestDTO createRequestDTO(final TrainingModel training, final String actionType) {
		return new TrainerWorkloadUpdateRequestDTO(
			training.getTrainer().getUserName(),
			training.getTrainer().getFirstName(),
			training.getTrainer().getLastName(),
			training.getTrainer().isActive(),
			training.getDate(),
			training.getDuration(),
			actionType
		);
	}

	private String performRequest(final TrainerWorkloadUpdateRequestDTO requestDTO) {
		final var requestUrl = trainerWorkloadServiceUrl + "/api/v1/trainer-workload/update";
		final var requestEntity = new HttpEntity<>(requestDTO);
		LOGGER.debug("[{}] In performRequest - Sending request to {}", MDC.get(TRANSACTION_ID), requestUrl);
		return circuitBreaker.run(
			() -> restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class).getBody(),
			this::requestFallback
		);
	}

	private String requestFallback(final Throwable throwable) {
		final String message = "Could not send request due to " + throwable.getMessage();
		LOGGER.error("In performRequest - {}", message);
		return message;
	}
}
