package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.WorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.DtoToModelMapper;
import com.epam.dmgolub.gym.service.WorkloadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.epam.dmgolub.gym.controller.TrainerWorkloadRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.ApiVersion.VERSION_1;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, consumes = APPLICATION_JSON_VALUE)
public class TrainerWorkloadRestController {

	public static final String URL = BASE_API_URL + VERSION_1 + "/trainer-workload";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadRestController.class);

	private final WorkloadService workloadService;
	private final DtoToModelMapper mapper;

	public TrainerWorkloadRestController(final WorkloadService workloadService, final DtoToModelMapper mapper) {
		this.workloadService = workloadService;
		this.mapper = mapper;
	}

	@PostMapping(value = "/update")
	@Operation(summary = "Handle ADD/DELETE training operation")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully completed the operation"),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<String> update(@RequestBody final WorkloadUpdateRequestDTO trainingRequest) {
		LOGGER.debug("[{}] In handleTrainingAction - Received a request: {}", MDC.get(TRANSACTION_ID), trainingRequest);
		String message;
		var status = HttpStatus.OK;
		if ("add".equalsIgnoreCase(trainingRequest.getActionType())) {
			workloadService.addWorkload(mapper.mapToWorkloadUpdateRequest(trainingRequest));
			message = "Training duration added successfully";
		} else if ("delete".equalsIgnoreCase(trainingRequest.getActionType())) {
			final var isDeleted = workloadService.deleteWorkload(mapper.mapToWorkloadUpdateRequest(trainingRequest));
			message = isDeleted ? "Training duration deleted successfully" : "Could not delete training";
		} else {
			message = "Invalid action type: " + trainingRequest.getActionType();
			status = HttpStatus.BAD_REQUEST;
		}
		LOGGER.debug("[{}] In handleTrainingAction - {}", MDC.get(TRANSACTION_ID), message);
		return new ResponseEntity<>(message, status);
	}
}
