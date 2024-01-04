package com.epam.dmgolub.gym.dto.constant;

public class Constants {

	public static final String FIRST_NAME_NOT_BLANK_MESSAGE = "First name can not be null or empty";
	public static final String FIRST_NAME_PATTERN_REGEXP = "^[a-zA-Z]*$";
	public static final String FIRST_NAME_PATTERN_MESSAGE = "First name may only contain letters from a-z and A-Z";
	public static final String LAST_NAME_NOT_BLANK_MESSAGE = "Last name can not be null or empty";
	public static final String LAST_NAME_PATTERN_REGEXP = "^[a-zA-Z]*$";
	public static final String LAST_NAME_PATTERN_MESSAGE = "Last name may only contain letters from a-z and A-Z";
	public static final String TRAINER_SPECIALIZATION_NOT_NULL_MESSAGE = "Specialization can not be null";
	public static final String TRAINEE_ID_NOT_NULL_MESSAGE = "Trainee can not be null";
	public static final String TRAINEE_ID_POSITIVE_MESSAGE = "Trainee id must be positive";
	public static final String TRAINER_ID_NOT_NULL_MESSAGE = "Trainer can not be null";
	public static final String TRAINER_ID_POSITIVE_MESSAGE = "Trainer id must be positive";
	public static final String TRAINING_NAME_NOT_BLANK_MESSAGE = "Training name can not be null or empty";
	public static final String TRAINING_TYPE_NOT_NULL_MESSAGE = "Training type cen not be null";
	public static final String TRAINING_DATE_NOT_NULL_MESSAGE = "Training date can not be null";
	public static final String TRAINING_DURATION_POSITIVE_MESSAGE = "Training duration must be a positive number";

	private Constants() {
		// Empty
	}
}
