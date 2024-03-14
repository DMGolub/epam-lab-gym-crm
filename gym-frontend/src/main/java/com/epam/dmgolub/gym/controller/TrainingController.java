package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.utility.ControllerUtilities;
import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINING_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAININGS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_TYPES;

@Controller
@RequestMapping("/trainings")
public class TrainingController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingController.class);

	private final TraineeService traineeService;
	private final TrainerService trainerService;
	private final TrainingService trainingService;
	private final TrainingTypeService trainingTypeService;

	public TrainingController(
		final TraineeService traineeService,
		final TrainerService trainerService,
		final TrainingService trainingService,
		final TrainingTypeService trainingTypeService
	) {
		this.traineeService = traineeService;
		this.trainerService = trainerService;
		this.trainingService = trainingService;
		this.trainingTypeService = trainingTypeService;
	}

	@PostMapping()
	public String save(
		@ModelAttribute(TRAINING) @Valid final TrainingCreateRequestDTO training,
		final BindingResult bindingResult,
		final Model model,
		final HttpSession session
	) {
		LOGGER.debug("In save -Validating new training");
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, NEW_TRAINING_VIEW_NAME);
			model.addAttribute(
				TRAINEE,
				traineeService.findByUserName(training.getTraineeUserName(), session)
			);
			model.addAttribute(
				TRAINERS,
				trainerService.findActiveTrainersAssignedOnTrainee(training.getTraineeUserName(), session)
			);
			return NEW_TRAINING_VIEW_NAME;
		}
		trainingService.save(training, session);
		LOGGER.debug("In save - Training saved successfully. Redirecting to training index view");
		return REDIRECT_TO_TRAINING_INDEX;
	}

	@GetMapping("/")
	public String findAll(final Model model) {
		final var trainingTypes = trainingTypeService.findAll();
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		LOGGER.debug("In findAll - Trainings fetched successfully. Returning training index view name");
		return TRAINING_INDEX_VIEW_NAME;
	}

	@GetMapping("/search-by-trainee")
	public String searchByTrainee(
		final @ModelAttribute @Valid TraineeTrainingsSearchRequestDTO traineeRequest,
		final BindingResult bindingResult,
		final Model model,
		final HttpSession session
	) {
		LOGGER.debug("In searchByTrainee - Received a search request={}", traineeRequest);
		final var trainingTypes = trainingTypeService.findAll();
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, TRAINING_INDEX_VIEW_NAME);
		} else {
			model.addAttribute(TRAININGS, trainingService.searchByTrainee(traineeRequest, session));
		}
		return TRAINING_INDEX_VIEW_NAME;
	}

	@GetMapping("/search-by-trainer")
	public String searchByTrainer(
		final @ModelAttribute @Valid TrainerTrainingsSearchRequestDTO trainerRequest,
		final BindingResult bindingResult,
		final Model model,
		final HttpSession session
	) {
		LOGGER.debug("In searchByTrainee - Received a search request={}", trainerRequest);
		model.addAttribute(TRAINING_TYPES, trainingTypeService.findAll());
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, TRAINING_INDEX_VIEW_NAME);
		} else {
			model.addAttribute(TRAININGS, trainingService.searchByTrainer(trainerRequest, session));
		}
		return TRAINING_INDEX_VIEW_NAME;
	}
}
