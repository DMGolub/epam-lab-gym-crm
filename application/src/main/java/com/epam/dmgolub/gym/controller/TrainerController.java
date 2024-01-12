package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_NEW_TRAINER;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINER_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_TYPES;

@Controller
@RequestMapping("trainers")
public class TrainerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerController.class);

	private final TrainerService trainerService;
	private final TrainingTypeService trainingTypeService;
	private final ModelToDtoMapper mapper;

	public TrainerController(
		final TrainerService trainerService,
		final TrainingTypeService trainingTypeService,
		final ModelToDtoMapper mapper
	) {
		this.trainerService = trainerService;
		this.trainingTypeService = trainingTypeService;
		this.mapper = mapper;
	}

	@GetMapping("/new")
	public String newTrainer(final Model model) {
		model.addAttribute(TRAINER, new TrainerRequestDTO());
		final var trainingTypes =  mapper.trainingTypeListToTrainingTypeDTOList(trainingTypeService.findAll());
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		LOGGER.debug("In newTrainer - Training types fetched successfully. Returning new trainer view name");
		return NEW_TRAINER_VIEW_NAME;
	}

	@PostMapping()
	public String save(
		@ModelAttribute(TRAINER) @Valid final TrainerRequestDTO trainer,
		final BindingResult bindingResult,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In save - validating new trainer");
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, NEW_TRAINER_VIEW_NAME);
			return NEW_TRAINER_VIEW_NAME;
		}
		trainerService.save(mapper.trainerRequestDTOToTrainer(trainer));
		LOGGER.debug("In save - trainer saved successfully. Redirecting to new trainer view");
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE, "Trainer added successfully");
		return REDIRECT_TO_NEW_TRAINER;
	}

	@PostMapping("/action")
	public String handleActionByUserName(
		@RequestParam final String action,
		@RequestParam final String userName,
		final Model model,
		final RedirectAttributes redirectAttributes
	) {
		LOGGER.debug("In handleAction - Received a request to {} trainer by userName={}", action, userName);
		try {
			final var trainer = trainerService.findByUserName(userName);
			switch (action.toLowerCase()) {
				case "find":
					model.addAttribute(TRAINER, mapper.trainerToTrainerResponseDTO(trainer));
					return REDIRECT_TO_TRAINER_INDEX + trainer.getId();
				default:
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE, "Invalid action");
					return REDIRECT_TO_TRAINER_INDEX;
			}
		} catch (final EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE,
				"Trainer with user name '" + userName + "' not found");
			return REDIRECT_TO_TRAINER_INDEX;
		}
	}

	@GetMapping("/{id:\\d+}/edit")
	public String edit(@PathVariable("id") final Long id, final Model model) {
		LOGGER.debug("In edit - fetching trainer with id={} from service", id);
		model.addAttribute(TRAINER, mapper.trainerToTrainerResponseDTO(trainerService.findById(id)));
		LOGGER.debug("In edit - fetching training types from service");
		final var trainingTypes = mapper.trainingTypeListToTrainingTypeDTOList(trainingTypeService.findAll());
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		LOGGER.debug("In edit - trainer and training types fetched successfully. Returning edit view name");
		return TRAINER_EDIT_VIEW_NAME;
	}

	@PutMapping("/{id:\\d+}")
	public String update(
		@PathVariable("id") final Long id,
		@ModelAttribute(TRAINER) @Valid final TrainerRequestDTO trainer,
		final BindingResult bindingResult
	) {
		LOGGER.debug("In update - validating updated trainer");
		ControllerUtilities.validateRequestId(id, trainer.getId());
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, TRAINER_EDIT_VIEW_NAME);
			return TRAINER_EDIT_VIEW_NAME;
		}
		trainerService.update(mapper.trainerRequestDTOToTrainer(trainer));
		LOGGER.debug("In update - trainer updated successfully. Redirecting to trainer index view");
		return REDIRECT_TO_TRAINER_INDEX;
	}

	@GetMapping("/{id:\\d+}")
	public String findById(@PathVariable("id") final Long id, final Model model) {
		model.addAttribute(TRAINER, mapper.trainerToTrainerResponseDTO(trainerService.findById(id)));
		LOGGER.debug("In findById - Trainer with id={} fetched successfully. Returning trainer view name", id);
		return TRAINER_VIEW_NAME;
	}

	@GetMapping("/")
	public String findAll(final Model model) {
		model.addAttribute(TRAINERS, mapper.trainerListToTrainerResponseDTOList(trainerService.findAll()));
		LOGGER.debug("In findAll - Trainers fetched successfully. Returning trainer index view name");
		return TRAINER_INDEX_VIEW_NAME;
	}
}
