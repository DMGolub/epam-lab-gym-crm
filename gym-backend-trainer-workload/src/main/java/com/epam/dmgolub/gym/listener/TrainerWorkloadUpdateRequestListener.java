package com.epam.dmgolub.gym.listener;

import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.DtoToModelMapper;
import com.epam.dmgolub.gym.service.WorkloadService;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@Component
public class TrainerWorkloadUpdateRequestListener {

	private static final String WORKLOAD_QUEUE_NAME = "workload";
	private static final String DLQ_NAME = "ActiveMQ.DLQ";

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadUpdateRequestListener.class);

	private final JmsTemplate jmsTemplate;
	private final Validator validator;
	private final WorkloadService workloadService;
	private final DtoToModelMapper mapper;

	public TrainerWorkloadUpdateRequestListener(
		final JmsTemplate jmsTemplate,
		final Validator validator,
		final WorkloadService workloadService,
		final DtoToModelMapper mapper
	) {
		this.jmsTemplate = jmsTemplate;
		this.validator = validator;
		this.workloadService = workloadService;
		this.mapper = mapper;
	}

	@JmsListener(destination = WORKLOAD_QUEUE_NAME)
	public void receiveRequest(final TrainerWorkloadUpdateRequestDTO request) {
		generateTransactionId();
		LOGGER.debug("[{}] In receiveRequest - Received: {}", MDC.get(TRANSACTION_ID), request);

		final var violations = validator.validate(request);
		if (!violations.isEmpty()) {
			sendToDLQ(request, violations.size() + " violation(s) found");
			return;
		}

		final var action = request.getActionType();
		if ("add".equalsIgnoreCase(action)) {
			workloadService.addWorkload(mapper.mapToWorkloadUpdateRequest(request));
		} else if ("delete".equalsIgnoreCase(action)) {
			workloadService.deleteWorkload(mapper.mapToWorkloadUpdateRequest(request));
		} else {
			sendToDLQ(request, "Invalid action type: " + action);
		}
	}

	private void generateTransactionId() {
		final String transactionId = UUID.randomUUID().toString();
		MDC.put(TRANSACTION_ID, transactionId);
	}

	private void sendToDLQ(final TrainerWorkloadUpdateRequestDTO request, final String message) {
		LOGGER.error("[{}] In receiveRequest - {}, sent request to DLQ", MDC.get(TRANSACTION_ID), message);
		jmsTemplate.convertAndSend(DLQ_NAME, request);
	}
}
