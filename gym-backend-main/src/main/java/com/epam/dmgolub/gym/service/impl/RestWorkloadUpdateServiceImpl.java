package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.service.WorkloadUpdateService;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static com.epam.dmgolub.gym.service.constant.Constants.ADD_WORKLOAD_ACTION_TYPE;
import static com.epam.dmgolub.gym.service.constant.Constants.DELETE_WORKLOAD_ACTION_TYPE;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;


@Service
public class RestWorkloadUpdateServiceImpl implements WorkloadUpdateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestWorkloadUpdateServiceImpl.class);

	private final WebClient webClient;
	private final CircuitBreaker circuitBreaker;

	@Value("${trainer-workload-service.url}")
	private String trainerWorkloadServiceUrl;

	public RestWorkloadUpdateServiceImpl(final WebClient webClient, final CircuitBreaker circuitBreaker) {
		this.webClient = webClient;
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
		final var requestUri = trainerWorkloadServiceUrl + "/api/v1/trainer-workload/update";
		LOGGER.debug("[{}] In performRequest - Sending request to {}", MDC.get(TRANSACTION_ID), requestUri);
		return circuitBreaker.run(
			() -> webClient.post().uri(requestUri)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestDTO)
				.attributes(clientRegistrationId("messaging-client-model"))
				.retrieve()
				.bodyToMono(String.class)
				.block(),
			this::requestFallback
		);
	}

	private String requestFallback(final Throwable cause) {
		final var message =
			String.format("Could not send request due to %s with message: %s", cause.getClass(), cause.getMessage());
		LOGGER.error("In performRequest - {}", message);
		return message;
	}
}
