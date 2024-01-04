package com.epam.dmgolub.gym.controller.constant;

public class Constants {

	public static final String TRAINEE = "trainee";
	public static final String TRAINEES = "trainees";
	public static final String NEW_TRAINEE_VIEW_NAME = "trainee/new";
	public static final String TRAINEE_EDIT_VIEW_NAME = "trainee/edit";
	public static final String TRAINEE_INDEX_VIEW_NAME = "trainee/index";
	public static final String REDIRECT_TO_TRAINEE_INDEX = "redirect:/trainees/";
	public static final String TRAINEE_VIEW_NAME = "trainee/trainee";

	public static final String TRAINER = "trainer";
	public static final String TRAINERS = "trainers";
	public static final String NEW_TRAINER_VIEW_NAME = "trainer/new";
	public static final String TRAINER_EDIT_VIEW_NAME = "trainer/edit";
	public static final String TRAINER_INDEX_VIEW_NAME = "trainer/index";
	public static final String REDIRECT_TO_TRAINER_INDEX = "redirect:/trainers/";
	public static final String TRAINER_VIEW_NAME = "trainer/trainer";

	public static final String TRAINING = "training";
	public static final String TRAININGS = "trainings";
	public static final String NEW_TRAINING_VIEW_NAME = "training/new";
	public static final String TRAINING_EDIT_VIEW_NAME = "training/edit";
	public static final String TRAINING_INDEX_VIEW_NAME = "training/index";
	public static final String REDIRECT_TO_TRAINING_INDEX = "redirect:/trainings/";
	public static final String TRAINING_VIEW_NAME = "training/training";

	public static final String TRAINING_TYPES = "trainingTypes";

	public static final String ENTITY_NOT_FOUND_VIEW = "exception/entity_not_found_exception.html";
	public static final String EXCEPTION_VIEW = "exception/exception";
	public static final String CONTROLLER_EXCEPTION_LOG_MESSAGE = "Request: {} caused an exception: {}";
	public static final String REQUEST_ID_INVALID_EXCEPTION_MESSAGE = "Request id=%d is not equal to path id=%d";
	public static final String BINDING_RESULT_ERROR_MESSAGE = "Validation failed. Returning {} view name";

	private Constants() {
		// empty
	}
}
