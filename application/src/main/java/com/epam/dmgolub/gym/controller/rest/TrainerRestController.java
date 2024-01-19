package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.dto.rest.CredentialsDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.TrainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.epam.dmgolub.gym.controller.rest.TrainerRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL,  consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Api(produces = APPLICATION_JSON_VALUE, value = "Operations for creating, updating and retrieving trainers")
public class TrainerRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainers";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerRestController.class);

	private final TrainerService trainerService;
	private final ModelToRestDtoMapper mapper;

	public TrainerRestController(final TrainerService trainerService, final ModelToRestDtoMapper mapper) {
		this.trainerService = trainerService;
		this.mapper = mapper;
	}

	@GetMapping
	@ApiOperation(value = "View all trainers", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved all trainers"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainerResponseDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all trainers");
		final var trainers = trainerService.findAll();
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/not-assigned-on")
	@ApiOperation(value = "View all trainers not assigned on a specific trainee", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved trainers not assigned on trainee"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> getNotAssignedOnTrainee(
		@RequestParam("userName") final String userName
	) {
		LOGGER.debug("In getNotAssignedOnTrainee - Received a request to get all trainers not assigned on {}", userName);
		final var trainers = trainerService.findActiveTrainersNotAssignedOnTrainee(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/assigned-on")
	@ApiOperation(value = "View all trainers assigned on a specific trainee", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved trainers assigned on trainee"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> getAssignedOnTrainee(
		@RequestParam("userName") final String userName
	) {
		LOGGER.debug("In getAssignedOnTrainee - Received a request to get all trainers assigned on {}", userName);
		final var trainers = trainerService.findActiveTrainersAssignedOnTrainee(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/profile")
	@ApiOperation(value = "Retrieve specific trainer with the supplied username", response = TrainerResponseDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved the trainer with the supplied username"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<TrainerResponseDTO> getByUserName(@RequestParam("userName") final String userName) {
		LOGGER.debug("In getByUserName - Received a request to get trainer={}", userName);
		final var trainer = trainerService.findByUserName(userName);
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTO(trainer), HttpStatus.OK);
	}

	@PostMapping
	@ApiOperation(value = "Create a trainer", response = CredentialsDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Successfully created new trainer"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<CredentialsDTO> create(@RequestBody @Valid final TrainerCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create trainer={}", request);
		final var trainer = trainerService.save(mapper.mapToTrainerModel(request));
		final var credentials = new CredentialsDTO(trainer.getUserName(), trainer.getPassword());
		return new ResponseEntity<>(credentials, HttpStatus.CREATED);
	}

	@PutMapping("/profile")
	@ApiOperation(value = "Update a specific trainer data", response = TrainerResponseDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully updated trainer"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<TrainerResponseDTO> update(@RequestBody @Valid final TrainerUpdateRequestDTO request) {
		LOGGER.debug("In update - Received a request to update trainer={}", request);
		final var trainer = trainerService.update(mapper.mapToTrainerModel(request));
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTO(trainer), HttpStatus.OK);
	}
}
