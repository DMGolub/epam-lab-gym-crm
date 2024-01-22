package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.dto.rest.CredentialsDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeUpdateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeUpdateTrainerListRequestDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.TraineeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

import javax.validation.Valid;
import java.util.List;

import static com.epam.dmgolub.gym.controller.rest.TraineeRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
@Api(produces = APPLICATION_JSON_VALUE, value = "Operations for creating, updating, retrieving and deleting trainees")
public class TraineeRestController {

	static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/trainees";
	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeRestController.class);

	private final TraineeService traineeService;
	private final ModelToRestDtoMapper mapper;

	public TraineeRestController(final TraineeService traineeService, final ModelToRestDtoMapper mapper) {
		this.traineeService = traineeService;
		this.mapper = mapper;
	}

	@GetMapping
	@ApiOperation(value = "View all trainees", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved all trainees"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all trainees");
		final var trainees = traineeService.findAll();
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTOList(trainees), HttpStatus.OK);
	}

	@GetMapping("/profile")
	@ApiOperation(value = "Retrieve specific trainee with the supplied username", response = TraineeResponseDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved the trainee with the supplied username"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<TraineeResponseDTO> getByUserName(@RequestParam("userName") final String userName) {
		LOGGER.debug("In getByUserName - Received a request to get trainee={}", userName);
		final var trainee = traineeService.findByUserName(userName);
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTO(trainee), HttpStatus.OK);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create a trainee", response = CredentialsDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 201, message = "Successfully created new trainee"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<CredentialsDTO> create(@RequestBody @Valid final TraineeCreateRequestDTO request) {
		LOGGER.debug("In create - Received a request to create trainee={}", request);
		final var trainee = traineeService.save(mapper.mapToTraineeModel(request));
		final var credentials = new CredentialsDTO(trainee.getUserName(), trainee.getPassword());
		return new ResponseEntity<>(credentials, HttpStatus.CREATED);
	}

	@PutMapping(value = "/profile", consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update a specific trainee data", response = TraineeResponseDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully updated trainee"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<TraineeResponseDTO> update(@RequestBody @Valid final TraineeUpdateRequestDTO request) {
		LOGGER.debug("In update - Received a request to update trainee={}", request);
		final var trainee = traineeService.update(mapper.mapToTraineeModel(request));
		return new ResponseEntity<>(mapper.mapToTraineeResponseDTO(trainee), HttpStatus.OK);
	}

	@PutMapping(value = "/profile/update-trainers", consumes = APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update a specific trainee trainer list", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully updated trainee trainer list"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TraineeResponseDTO.TrainerDTO>> updateTrainerList(
		@RequestBody @Valid final TraineeUpdateTrainerListRequestDTO request
	) {
		LOGGER.debug("In updateTrainerList - Received a request to update trainee trainer list={}", request);
		traineeService.updateTrainers(request.getTraineeUserName(), request.getTrainerUserNames());
		final var trainers = traineeService.findByUserName(request.getTraineeUserName()).getTrainers();
		return new ResponseEntity<>(
			mapper.mapTraineeModelTrainerListToTraineeResponseDTOTrainerDTOList(trainers), HttpStatus.OK);
	}

	@DeleteMapping("/profile")
	@ApiOperation(value = "Deletes a specific trainee with supplied username")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully deleted the specific trainee"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to delete is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	@ResponseStatus(HttpStatus.OK)
	public void delete(@RequestParam("userName") final String userName) {
		LOGGER.debug("In delete - Received a request to delete trainee={}", userName);
		traineeService.delete(userName);
	}
}
