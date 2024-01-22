package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.dto.rest.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.epam.dmgolub.gym.controller.rest.TrainingTypeRestController.URL;
import static com.epam.dmgolub.gym.controller.rest.constant.Constants.BASE_API_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = URL, produces = APPLICATION_JSON_VALUE)
public class TrainingTypeRestController {

	public static final String URL = BASE_API_URL + ApiVersion.VERSION_1 + "/training-types";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeRestController.class);

	private final TrainingTypeService trainingTypeService;
	private final ModelToRestDtoMapper mapper;

	public TrainingTypeRestController(
		final TrainingTypeService trainingTypeService,
		final ModelToRestDtoMapper mapper
	) {
		this.trainingTypeService = trainingTypeService;
		this.mapper = mapper;
	}

	@GetMapping
	@ApiOperation(value = "View all training types", response = List.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved all training types"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainingTypeDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all training types");
		final var types = trainingTypeService.findAll();
		return new ResponseEntity<>(mapper.mapToTrainingTypeDTOList(types), HttpStatus.OK);
	}

	@GetMapping("/{id:\\d+}")
	@ApiOperation(value = "Retrieve specific training type with the supplied id", response = TrainingTypeDTO.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved the training type with the supplied id"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
		@ApiResponse(code = 500, message = "Application failed to process the request")
	})
	public ResponseEntity<TrainingTypeDTO> getById(@PathVariable("id") final Long id) {
		LOGGER.debug("In getById - Received a request to get training type by id={}", id);
		final var training = trainingTypeService.findById(id);
		return new ResponseEntity<>(mapper.mapToTrainingTypeDTO(training), HttpStatus.OK);
	}
}
