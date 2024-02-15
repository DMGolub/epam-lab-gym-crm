package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.utility.ControllerUtils;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.security.service.TokenService;
import com.epam.dmgolub.gym.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import jakarta.validation.Valid;
import java.util.List;

import static com.epam.dmgolub.gym.controller.TrainerRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class TrainerRestController {

	public static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainers";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerRestController.class);

	private final TrainerService trainerService;
	private final TokenService tokenService;
	private final ModelToRestDtoMapper mapper;

	public TrainerRestController(
		final TrainerService trainerService,
		final TokenService tokenService,
		final ModelToRestDtoMapper mapper
	) {
		this.trainerService = trainerService;
		this.tokenService = tokenService;
		this.mapper = mapper;
	}

	@GetMapping
	@Operation(summary = "View all trainers")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved all trainers",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TrainerResponseDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainerResponseDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all trainers");
		final var trainers = trainerService.findAll();
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/not-assigned-on")
	@Operation(summary = "View all trainers not assigned on a specific trainee")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainers not assigned on trainee",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeResponseDTO.TrainerDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> getNotAssignedOnTrainee(
		@RequestParam("userName") final String userName
	) {
		LOGGER.debug("In getNotAssignedOnTrainee - Received a request to get all trainers not assigned on {}", userName);
		final var trainers = trainerService.findActiveTrainersNotAssignedOnTrainee(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/assigned-on")
	@Operation(summary = "View all trainers assigned on a specific trainee")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainers assigned on trainee",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeResponseDTO.TrainerDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> getAssignedOnTrainee(
		@RequestParam("userName") final String userName
	) {
		LOGGER.debug("In getAssignedOnTrainee - Received a request to get all trainers assigned on {}", userName);
		final var trainers = trainerService.findActiveTrainersAssignedOnTrainee(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@GetMapping("/profile")
	@Operation(summary = "Retrieve specific trainer with the supplied username")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainer with the supplied username",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = TrainerResponseDTO.class))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The trainer you were trying to get is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<TrainerResponseDTO> getByUserName(@RequestParam("userName") final String userName) {
		LOGGER.debug("In getByUserName - Received a request to get trainer={}", userName);
		final var trainer = trainerService.findByUserName(userName);
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTO(trainer), HttpStatus.OK);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a trainer")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Successfully created new trainer",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = CredentialsDTO.class))}),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<SignUpResponseDTO> create(@RequestBody @Valid final TrainerCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create trainer={}", request);
		final var credentials = trainerService.save(mapper.mapToTrainerModel(request));
		final var token = tokenService.generateToken(credentials.getUserName());
		final var response = new SignUpResponseDTO(token, mapper.mapToCredentialsDTO(credentials));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/profile", consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Update a specific trainer data")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully updated trainer",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = TrainerResponseDTO.class))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The trainer you were trying to update is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<TrainerResponseDTO> update(@RequestBody @Valid final TrainerUpdateRequestDTO request) {
		LOGGER.debug("In update - Received a request to update trainer={}", request);
		ControllerUtils.checkIsAuthorizedUser(request.getUserName());
		final var trainer = trainerService.update(mapper.mapToTrainerModel(request));
		return new ResponseEntity<>(mapper.mapToTrainerResponseDTO(trainer), HttpStatus.OK);
	}
}
