package com.epam.dmgolub.gym.controller.mvc;

import com.epam.dmgolub.gym.controller.ControllerUtilities;
import com.epam.dmgolub.gym.dto.mvc.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingRequestDTO;
import com.epam.dmgolub.gym.mapper.mvc.ModelToDtoMapper;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.AVAILABLE_TRAINERS;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.NEW_TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_NEW_TRAINEE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_TRAINEE_INDEX;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEES;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINING;

@Controller
@RequestMapping("trainees")
public class TraineeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeController.class);

	private final TraineeService traineeService;
	private final TrainerService trainerService;
	private final ModelToDtoMapper mapper;

	public TraineeController(
		final TraineeService traineeService,
		final TrainerService trainerService,
		final ModelToDtoMapper mapper
	) {
		this.traineeService = traineeService;
		this.trainerService = trainerService;
		this.mapper = mapper;
	}

	@GetMapping("/new")
	public String newTrainee(final Model model) {
		model.addAttribute(TRAINEE, new TraineeRequestDTO());
		LOGGER.debug("In newTrainee - Returning new trainee view name");
		return NEW_TRAINEE_VIEW_NAME;
	}

	@PostMapping()
	public String save(
		@ModelAttribute(TRAINEE) @Valid final TraineeRequestDTO trainee,
		final BindingResult bindingResult,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In save - validating new trainee");
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, NEW_TRAINEE_VIEW_NAME);
			return NEW_TRAINEE_VIEW_NAME;
		}
		traineeService.save(mapper.mapToTraineeModel(trainee));
		LOGGER.debug("In save - trainee saved successfully. Redirecting to new trainee view");
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE, "Trainee added successfully");
		return REDIRECT_TO_NEW_TRAINEE;
	}

	@PostMapping("/action")
	public String handleActionByUserName(
		@RequestParam final String action,
		@RequestParam final String userName,
		final Model model,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In handleAction - Received a request to {} trainee by userName={}", action, userName);
		try {
			final var trainee = traineeService.findByUserName(userName);
			switch (action.toLowerCase()) {
				case "find":
					model.addAttribute(TRAINEE, mapper.mapToTraineeResponseDTO(trainee));
					return REDIRECT_TO_TRAINEE_INDEX + trainee.getId();
				case "delete":
					traineeService.delete(userName);
					redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE,
						"Trainee '" + userName + "' and all related trainings deleted successfully");
					return REDIRECT_TO_TRAINEE_INDEX;
				default:
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE, "Invalid action");
					return REDIRECT_TO_TRAINEE_INDEX;
			}
		} catch (final EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE,
				"Trainee with user name '" + userName + "' not found");
			return REDIRECT_TO_TRAINEE_INDEX;
		}
	}

	@GetMapping("/{id:\\d+}/edit")
	public String edit(@PathVariable final Long id, final Model model) {
		LOGGER.debug("In edit - fetching trainee with id={} from service", id);
		model.addAttribute(TRAINEE, mapper.mapToTraineeResponseDTO(traineeService.findById(id)));
		LOGGER.debug("In edit - trainee fetched successfully. Returning edit view name");
		return TRAINEE_EDIT_VIEW_NAME;
	}

	@PutMapping("/{id:\\d+}")
	public String update(
		@PathVariable("id") final Long id,
		@ModelAttribute @Valid final TraineeRequestDTO trainee,
		final BindingResult bindingResult
	) {
		ControllerUtilities.validateRequestId(id, trainee.getId());
		LOGGER.debug("In update - validating updated trainee");
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, TRAINEE_EDIT_VIEW_NAME);
			return TRAINEE_EDIT_VIEW_NAME;
		}
		traineeService.update(mapper.mapToTraineeModel(trainee));
		LOGGER.debug("In update - trainee updated successfully. Redirecting to trainee index view");
		return REDIRECT_TO_TRAINEE_INDEX;
	}

	@PutMapping("/{id:\\d+}/add-trainer")
	public String addTrainer(
		@PathVariable("id") final Long traineeId,
		@RequestParam("trainerId") final Long trainerId,
		final Model model
	) {
		LOGGER.debug("In addTrainer - Received request to add trainer={} to trainee={}", trainerId, traineeId);
		traineeService.addTrainer(traineeId, trainerId);
		model.addAttribute(TRAINEE, mapper.mapToTraineeResponseDTO(traineeService.findById(traineeId)));
		final var trainers = trainerService.findActiveTrainersNotAssignedOnTrainee(traineeId);
		model.addAttribute(AVAILABLE_TRAINERS, mapper.mapToTrainerResponseDTOList(trainers));
		return REDIRECT_TO_TRAINEE_INDEX + traineeId;
	}

	@GetMapping("/{id:\\d+}/add-training")
	public String addTraining(@PathVariable("id") final Long traineeId, final Model model) {
		model.addAttribute(TRAINING, new TrainingRequestDTO());
		model.addAttribute(TRAINEE, mapper.mapToTraineeResponseDTO(traineeService.findById(traineeId)));
		final var trainers = trainerService.findActiveTrainersAssignedOnTrainee(traineeId);
		model.addAttribute(TRAINERS, mapper.mapToTrainerResponseDTOList(trainers));
		LOGGER.debug("In newTraining - All data fetched successfully. Returning new training view name");
		return NEW_TRAINING_VIEW_NAME;
	}

	@DeleteMapping("/{id:\\d+}")
	public String delete(@PathVariable("id") final Long id) {
		LOGGER.debug("In delete - removing trainee with id={}", id);
		traineeService.delete(id);
		return REDIRECT_TO_TRAINEE_INDEX;
	}

	@GetMapping("/{id:\\d+}")
	public String findById(@PathVariable("id") final Long id, final Model model) {
		model.addAttribute(TRAINEE, mapper.mapToTraineeResponseDTO(traineeService.findById(id)));
		LOGGER.debug("In findById - Trainee with id={} fetched successfully. Returning trainee view name", id);
		final var trainers = trainerService.findActiveTrainersNotAssignedOnTrainee(id);
		model.addAttribute(AVAILABLE_TRAINERS, mapper.mapToTrainerResponseDTOList(trainers));
		return TRAINEE_VIEW_NAME;
	}

	@GetMapping("/")
	public String findAll(final Model model) {
		model.addAttribute(TRAINEES, mapper.mapToTraineeResponseDTOList(traineeService.findAll()));
		LOGGER.debug("In findAll - Trainees fetched successfully. Returning trainee index view name");
		return TRAINEE_INDEX_VIEW_NAME;
	}
}
