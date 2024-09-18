package com.epam.dmgolub.gym.controller.constant;

public class Constants {

	public static final String TRAINEE = "trainee";
	public static final String TRAINEES = "trainees";
	public static final String NEW_TRAINEE_VIEW_NAME = "trainee/new";
	public static final String TRAINEE_EDIT_VIEW_NAME = "trainee/edit";
	public static final String TRAINEE_INDEX_VIEW_NAME = "trainee/index";
	public static final String REDIRECT_TO_TRAINEE_INDEX = "redirect:/trainees/";
	public static final String REDIRECT_TO_TRAINEE_PROFILE = "redirect:/trainees/profile?userName=";
	public static final String REDIRECT_TO_NEW_TRAINEE = "redirect:/trainees/new";
	public static final String TRAINEE_VIEW_NAME = "trainee/trainee";
	public static final String AVAILABLE_TRAINERS = "availableTrainers";

	public static final String TRAINER = "trainer";
	public static final String TRAINERS = "trainers";
	public static final String NEW_TRAINER_VIEW_NAME = "trainer/new";
	public static final String TRAINER_EDIT_VIEW_NAME = "trainer/edit";
	public static final String TRAINER_INDEX_VIEW_NAME = "trainer/index";
	public static final String REDIRECT_TO_TRAINER_INDEX = "redirect:/trainers/";
	public static final String REDIRECT_TO_TRAINER_PROFILE = "redirect:/trainers/profile?userName=";
	public static final String REDIRECT_TO_NEW_TRAINER = "redirect:/trainers/new";
	public static final String TRAINER_VIEW_NAME = "trainer/trainer";

	public static final String TRAINING = "training";
	public static final String TRAININGS = "trainings";
	public static final String NEW_TRAINING_VIEW_NAME = "training/new";
	public static final String TRAINING_INDEX_VIEW_NAME = "training/index";
	public static final String REDIRECT_TO_TRAINING_INDEX = "redirect:/trainings/";

	public static final String TRAINING_TYPES = "trainingTypes";

	public static final String UNAUTHENTICATED_VIEW = "error/401";
	public static final String FORBIDDEN_VIEW = "error/403";
	public static final String ENTITY_NOT_FOUND_VIEW = "error/404";
	public static final String EXCEPTION_VIEW = "error/500";
	public static final String CONTROLLER_EXCEPTION_LOG_MESSAGE = "Request: {} caused an exception: {}";
	public static final String BINDING_RESULT_ERROR_MESSAGE = "Validation failed. Returning {} view name";

	public static final String ERROR_MESSAGE_ATTRIBUTE = "errorMessage";
	public static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";

	public static final String LOGIN = "login";
	public static final String LOGIN_INDEX_VIEW_NAME = "login/index";
	public static final String REDIRECT_TO_LOGIN_INDEX = "redirect:/login";

	public static final String CHANGE_PASSWORD_REQUEST = "changePasswordRequest";
	public static final String USER_NAME = "userName";
	public static final String PROFILE_INDEX_VIEW_NAME = "profile/index";
	public static final String REDIRECT_TO_PROFILE_INDEX = "redirect:/profile/";

	public static final String JWT_TOKEN = "jwtToken";

	private Constants() {
		// empty
	}
}
