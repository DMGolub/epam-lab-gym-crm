package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.utility.ControllerUtilities;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.JWT_TOKEN;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_NEW_TRAINER;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINER_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINER_PROFILE;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_TYPES;

@Controller
@RequestMapping("trainers")
public class TrainerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerController.class);

	private final TrainerService trainerService;
	private final TrainingTypeService trainingTypeService;

	public TrainerController(
		final TrainerService trainerService,
		final TrainingTypeService trainingTypeService
	) {
		this.trainerService = trainerService;
		this.trainingTypeService = trainingTypeService;
	}

	@GetMapping("/new")
	public String newTrainer(final Model model) {
		model.addAttribute(TRAINER, new TrainerRequestDTO());
		final var trainingTypes =  trainingTypeService.findAll();
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		LOGGER.debug("In newTrainer - Training types fetched successfully. Returning new trainer view name");
		return NEW_TRAINER_VIEW_NAME;
	}

	@PostMapping
	public String save(
		@ModelAttribute(TRAINER) @Valid final TrainerRequestDTO trainer,
		final BindingResult bindingResult,
		final RedirectAttributes redirectAttributes,
		final HttpSession session
	) {
		LOGGER.debug("In save - validating new trainer");
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, NEW_TRAINER_VIEW_NAME);
			return NEW_TRAINER_VIEW_NAME;
		}
		final var response = trainerService.save(trainer);
		LOGGER.debug("In save - Trainer saved successfully. Redirecting to new trainer view");
		final String successMessage = "Trainer added successfully, username: %s, password: %s";
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_ATTRIBUTE,
			String.format(successMessage, response.getCredentials().getUserName(), response.getCredentials().getPassword()));
		session.setAttribute(JWT_TOKEN, response.getToken());
		return REDIRECT_TO_NEW_TRAINER;
	}

	@PostMapping("/action")
	public String handleActionByUserName(
		@RequestParam final String action,
		@RequestParam final String userName,
		final Model model,
		final RedirectAttributes redirectAttributes,
		final HttpSession session
	) {
		LOGGER.debug("In handleAction - Received a request to {} trainer by userName={}", action, userName);
		try {
			final var trainer = trainerService.findByUserName(userName, session);
			if ("find".equalsIgnoreCase(action)) {
				model.addAttribute(TRAINER, trainer);
				return REDIRECT_TO_TRAINER_PROFILE + userName;
			} else {
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE, "Invalid action");
				return REDIRECT_TO_TRAINER_INDEX;
			}
		} catch (final EntityNotFoundException e) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_ATTRIBUTE,
				"Trainer with user name '" + userName + "' not found");
			return REDIRECT_TO_TRAINER_INDEX;
		}
	}

	@GetMapping("/edit")
	public String edit(
		@RequestParam final String userName,
		final Model model,
		final HttpSession session
	) {
		LOGGER.debug("In edit - fetching trainer with userName={} from service", userName);
		model.addAttribute(TRAINER, trainerService.findByUserName(userName, session));
		LOGGER.debug("In edit - fetching training types from service");
		final var trainingTypes = trainingTypeService.findAll();
		model.addAttribute(TRAINING_TYPES, trainingTypes);
		LOGGER.debug("In edit - trainer and training types fetched successfully. Returning edit view name");
		return TRAINER_EDIT_VIEW_NAME;
	}

	@PutMapping("/profile")
	public String update(
		@ModelAttribute(TRAINER) @Valid final TrainerRequestDTO trainer,
		final BindingResult bindingResult,
		final HttpSession session
	) {
		LOGGER.debug("In update - Validating updated trainer {}", trainer);
		if (bindingResult.hasErrors()) {
			ControllerUtilities.logBingingResultErrors(bindingResult, LOGGER, TRAINER_EDIT_VIEW_NAME);
			return TRAINER_EDIT_VIEW_NAME;
		}
		trainerService.update(trainer, session);
		LOGGER.debug("In update - Trainer updated successfully. Redirecting to trainer index view");
		return REDIRECT_TO_TRAINER_INDEX;
	}

	@GetMapping("/profile")
	public String findByUserName(
		@RequestParam final String userName,
		final Model model,
		final HttpSession session
	) {
		model.addAttribute(TRAINER, trainerService.findByUserName(userName, session));
		LOGGER.debug("In findByUserName - Trainer {} fetched successfully. Returning trainer view name", userName);
		return TRAINER_VIEW_NAME;
	}

	@GetMapping("/")
	public String findAll(final Model model, final HttpSession session) {
		model.addAttribute(TRAINERS, trainerService.findAll(session));
		LOGGER.debug("In findAll - Trainers fetched successfully. Returning trainer index view name");
		return TRAINER_INDEX_VIEW_NAME;
	}
}
