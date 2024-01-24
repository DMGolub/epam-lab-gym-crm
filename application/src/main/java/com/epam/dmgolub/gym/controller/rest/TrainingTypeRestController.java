package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.controller.rest.constant.ApiVersion;
import com.epam.dmgolub.gym.dto.rest.TrainingTypeDTO;
import com.epam.dmgolub.gym.mapper.rest.ModelToRestDtoMapper;
import com.epam.dmgolub.gym.service.TrainingTypeService;
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
	@Operation(summary = "View all training types")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved all training types",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				array = @ArraySchema(schema = @Schema(implementation = TrainingTypeDTO.class)))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<List<TrainingTypeDTO>> getAll() {
		LOGGER.debug("In getAll - Received a request to get all training types");
		final var types = trainingTypeService.findAll();
		return new ResponseEntity<>(mapper.mapToTrainingTypeDTOList(types), HttpStatus.OK);
	}

	@GetMapping("/{id:\\d+}")
	@Operation(summary = "Retrieve specific training type with the supplied id")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Successfully retrieved the training type with the supplied id",
			content = {@Content(mediaType = APPLICATION_JSON_VALUE,
				schema = @Schema(implementation = TrainingTypeDTO.class))}),
		@ApiResponse(responseCode = "401", description = "You are not authorized to access the resource"),
		@ApiResponse(responseCode = "404", description = "The training type you were trying to get is not found"),
		@ApiResponse(responseCode = "500", description = "Application failed to process the request")
	})
	public ResponseEntity<TrainingTypeDTO> getById(@PathVariable("id") final Long id) {
		LOGGER.debug("In getById - Received a request to get training type by id={}", id);
		final var training = trainingTypeService.findById(id);
		return new ResponseEntity<>(mapper.mapToTrainingTypeDTO(training), HttpStatus.OK);
	}
}
