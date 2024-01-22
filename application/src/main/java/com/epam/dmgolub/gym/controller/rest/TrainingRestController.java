package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.dto.rest.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.TrainingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.epam.dmgolub.gym.controller.rest.TrainingRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class TrainingRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainings";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingRestController.class);

	private final TrainingService trainingService;
	private final ModelToRestDtoMapper mapper;

	public TrainingRestController(final TrainingService trainingService, final ModelToRestDtoMapper mapper) {
		this.trainingService = trainingService;
		this.mapper = mapper;
	}

	@GetMapping(value = "/search-by-trainee", consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "View all trainings associated with a specific trainee", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved trainings associated with trainee"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeTrainingResponseDTO>> searchByTrainee(
		@RequestBody @Valid final TraineeTrainingsSearchRequestDTO request
	) {
		LOGGER.debug("In searchByTrainee - Received a request to get trainings for trainee {}",
			request.getUserName());
		final var trainings = trainingService.searchByTrainee(mapper.mapToTraineeTrainingsSearchRequest(request));
		return new ResponseEntity<>(mapper.mapToTraineeTrainingResponseDTOList(trainings), HttpStatus.OK);
	}

	@GetMapping(value = "/search-by-trainer", consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "View all trainings associated with a specific trainer", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved trainings associated with trainer"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainerTrainingResponseDTO>> searchByTrainer(
		@RequestBody @Valid final TrainerTrainingsSearchRequestDTO request
	) {
		LOGGER.debug("In searchByTrainee - Received a request to get trainings for trainer {}",
			request.getUserName());
		final var trainings = trainingService.searchByTrainer(mapper.mapToTrainerTrainingsSearchRequest(request));
		return new ResponseEntity<>(mapper.mapToTrainerTrainingResponseDTOList(trainings), HttpStatus.OK);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a training")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully created new training"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	@ResponseStatus(HttpStatus.OK)
	public void create(@RequestBody @Valid final TrainingCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create training={}", request);
		trainingService.save(mapper.mapToTrainingModel(request));
	}
}
