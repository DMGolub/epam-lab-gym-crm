package com.epam.dmgolub.gym.dto.constant;

public class Constants {

	public static final String FIRST_NAME_PATTERN_REGEXP = "^[a-zA-Z]{2,30}$";
	public static final String LAST_NAME_PATTERN_REGEXP = "^[a-zA-Z]{2,30}$";
	public static final String USERNAME_PATTERN_REGEXP = "^[a-zA-Z]{2,30}\\.[a-zA-Z]{2,30}[0-9]*$";
	public static final String TRAINING_NAME_PATTERN_REGEXP = "^[a-zA-Z0-9]{3,30}$";

	private Constants() {
		// Empty
	}
}
