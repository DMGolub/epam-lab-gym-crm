package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.utility.ControllerUtils;
import com.epam.dmgolub.gym.dto.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.Date;
import java.util.List;

import static com.epam.dmgolub.gym.controller.TrainingRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class TrainingRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainings";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingRestController.class);

	private final TrainingService trainingService;
	private final TrainingTypeService trainingTypeService;
	private final ModelToRestDtoMapper mapper;

	public TrainingRestController(
		final TrainingService trainingService,
		final TrainingTypeService trainingTypeService,
		final ModelToRestDtoMapper mapper
	) {
		this.trainingService = trainingService;
		this.trainingTypeService = trainingTypeService;
		this.mapper = mapper;
	}

	@GetMapping(value = "/search-by-trainee")
	@Operation(summary = "Search for trainings associated with a specific trainee by params")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainings associated with trainee",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeTrainingResponseDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeTrainingResponseDTO>> searchByTrainee(
		@RequestParam @NotBlank final String traineeUserName,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final Date periodFrom,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final Date periodTo,
		@RequestParam(required = false) final String trainerUserName,
		@RequestParam(required = false) final Long trainingTypeId
	) {
		LOGGER.debug("In searchByTrainee - Received a request to get trainings for trainee={}", traineeUserName);
		ControllerUtils.checkIsAuthorizedUser(traineeUserName);
		final var trainings = trainingService.searchByTrainee(new TraineeTrainingsSearchRequest(
			traineeUserName,
			periodFrom,
			periodTo,
			trainerUserName,
			trainingTypeId != null ? trainingTypeService.findById(trainingTypeId) : null
		));
		return new ResponseEntity<>(mapper.mapToTraineeTrainingResponseDTOList(trainings), HttpStatus.OK);
	}

	@GetMapping(value = "/search-by-trainer")
	@Operation(summary = "Search for trainings associated with a specific trainer by params")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainings associated with trainer",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeTrainingResponseDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainerTrainingResponseDTO>> searchByTrainer(
		@RequestParam @NotBlank final String trainerUserName,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final Date periodFrom,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final Date periodTo,
		@RequestParam(required = false) final String traineeUserName
	) {
		LOGGER.debug("In searchByTrainer - Received a request to get trainings for trainer={}", trainerUserName);
		ControllerUtils.checkIsAuthorizedUser(trainerUserName);
		final var request = new TrainerTrainingsSearchRequest(trainerUserName, periodFrom, periodTo, traineeUserName);
		final var trainings = trainingService.searchByTrainer(request);
		return new ResponseEntity<>(mapper.mapToTrainerTrainingResponseDTOList(trainings), HttpStatus.OK);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a training")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully created new training"),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	@ResponseStatus(HttpStatus.OK)
	public void create(@RequestBody @Valid final TrainingCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create training={}", request);
		ControllerUtils.checkIsAuthorizedUser(request.getTraineeUserName());
		trainingService.save(mapper.mapToTrainingModel(request));
	}
}
