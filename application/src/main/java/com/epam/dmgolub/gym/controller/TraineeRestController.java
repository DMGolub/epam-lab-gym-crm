package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.constant.ApiVersion;
import com.epam.dmgolub.gym.controller.utility.ControllerUtils;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeUpdateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeUpdateTrainerListRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.security.service.TokenService;
import com.epam.dmgolub.gym.service.TraineeService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

import static com.epam.dmgolub.gym.controller.TraineeRestController.URL;
import static com.epam.dmgolub.gym.controller.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class TraineeRestController {

	public static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainees";
	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeRestController.class);

	private final TraineeService traineeService;
	private final TokenService tokenService;
	private final ModelToRestDtoMapper mapper;

	public TraineeRestController(
		final TraineeService traineeService,
		final TokenService tokenService,
		final ModelToRestDtoMapper mapper
	) {
		this.traineeService = traineeService;
		this.tokenService = tokenService;
		this.mapper = mapper;
	}

	@GetMapping
	@Operation(summary = "View all trainees")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved all trainees",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeResponseDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all trainees");
		final var trainees = traineeService.findAll();
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOList(trainees), HttpStatus.OK);
	}

	@GetMapping("/profile")
	@Operation(summary = "Retrieve specific trainee with the supplied username")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved trainee with the supplied username",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = TraineeResponseDTO.class))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The trainee you were trying to get is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<TraineeResponseDTO> getByUserName(@RequestParam("userName") final String userName) {
		LOGGER.debug("In getByUserName - Received a request to get trainee={}", userName);
		ControllerUtils.checkIsAuthorizedUser(userName);
		final var trainee = traineeService.findByUserName(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTO(trainee), HttpStatus.OK);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Create a trainee")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Successfully created new trainee",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = CredentialsDTO.class))}),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<SignUpResponseDTO> create(@RequestBody @Valid final TraineeCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create trainee={}", request);
		final var credentials = traineeService.save(mapper.mapToTraineeModel(request));
		final var token = tokenService.generateToken(credentials.getUserName());
		final var response = new SignUpResponseDTO(token, mapper.mapToCredentialsDTO(credentials));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PutMapping(value = "/profile", consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Update a specific trainee data")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully updated trainee",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = TraineeResponseDTO.class))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The trainee you were trying to update is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<TraineeResponseDTO> update(@RequestBody @Valid final TraineeUpdateRequestDTO request) {
		LOGGER.debug("In update - Received a request to update trainee={}", request);
		ControllerUtils.checkIsAuthorizedUser(request.getUserName());
		final var trainee = traineeService.update(mapper.mapToTraineeModel(request));
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTO(trainee), HttpStatus.OK);
	}

	@PutMapping(value = "/profile/update-trainers", consumes = APPLICATION_JSON_VALUE)
	@Operation(summary = "Update a specific trainee trainer list")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully updated trainee trainer list",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TraineeResponseDTO.TrainerDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The user you were trying to update is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> updateTrainerList(
		@RequestBody @Valid final TraineeUpdateTrainerListRequestDTO request
	) {
		LOGGER.debug("In updateTrainerList - Received a request to update trainee trainer list={}", request);
		ControllerUtils.checkIsAuthorizedUser(request.getTraineeUserName());
		traineeService.updateTrainers(request.getTraineeUserName(), request.getTrainerUserNames());
		final var trainers = traineeService.findByUserName(request.getTraineeUserName()).getTrainers();
		return new ResponseEntity<>(
			mapper.mapTraineeModelTrainerListToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@DeleteMapping("/profile")
	@Operation(summary = "Deletes a specific trainee with supplied username")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully deleted the specific trainee"),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The trainee you were trying to delete is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestParam("userName") final String userName) {
		LOGGER.debug("In delete - Received a request to delete trainee={}", userName);
		ControllerUtils.checkIsAuthorizedUser(userName);
		traineeService.delete(userName);
	}
}
