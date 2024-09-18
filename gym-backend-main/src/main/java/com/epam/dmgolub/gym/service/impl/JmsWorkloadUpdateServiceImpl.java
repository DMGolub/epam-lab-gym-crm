package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.model.TrainingModel;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static com.epam.dmgolub.gym.service.constant.Constants.ADD_WORKLOAD_ACTION_TYPE;
import static com.epam.dmgolub.gym.service.constant.Constants.DELETE_WORKLOAD_ACTION_TYPE;

@Component
@Qualifier("jms")
public class JmsWorkloadUpdateServiceImpl extends AbstractWorkloadUpdateService {

	private static final String WORKLOAD_QUEUE_NAME = "workload";
	private static final Logger LOGGER = LoggerFactory.getLogger(JmsWorkloadUpdateServiceImpl.class);

	private final JmsTemplate jmsTemplate;

	public JmsWorkloadUpdateServiceImpl(final JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void add(final TrainingModel training) {
		LOGGER.debug("[{}] In add - Received request to add training to workload {}",
			MDC.get(TRANSACTION_ID), training);
		sendRequest(training, ADD_WORKLOAD_ACTION_TYPE);
	}

	@Override
	public void delete(final TrainingModel training) {
		LOGGER.debug("[{}] In delete - Received request to delete training from workload {}",
			MDC.get(TRANSACTION_ID), training);
		sendRequest(training, DELETE_WORKLOAD_ACTION_TYPE);
	}

	private void sendRequest(final TrainingModel training, final String actionType) {
		final var request = createRequestDTO(training, actionType);
		try {
			jmsTemplate.convertAndSend(WORKLOAD_QUEUE_NAME, request);
			LOGGER.debug("[{}] In sendRequest - Sent {} to {}", MDC.get(TRANSACTION_ID), request, WORKLOAD_QUEUE_NAME);
		} catch (final JmsException e) {
			LOGGER.error("[{}] In sendRequest - Failed to send request due to {} with message: {}",
				MDC.get(TRANSACTION_ID), e.getClass().getSimpleName(), e.getMessage());
			LOGGER.error("[{}] In sendRequest - Workload should be updated manually with the following: {}",
				MDC.get(TRANSACTION_ID), request);
		}
	}
}
