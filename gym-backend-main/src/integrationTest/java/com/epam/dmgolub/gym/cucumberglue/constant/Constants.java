package com.epam.dmgolub.gym.cucumberglue.constant;

public class Constants {

	public static final String LOCALHOST_URL = "http://localhost:";
	public static final String PROFILE = "/profile";
	public static final String PROFILE_WITH_USERNAME = PROFILE + "?userName=";
	public static final String NOT_ASSIGNED_ON_WITH_USERNAME = "/not-assigned-on?userName=";
	public static final String ASSIGNED_ON_WITH_USERNAME = "/assigned-on?userName=";
	public static final String UPDATE_TRAINERS = "/update-trainers";
	public static final String SEARCH_BY_TRAINEE = "/search-by-trainee";
	public static final String SEARCH_BY_TRAINER = "/search-by-trainer";

	private Constants() {
		// Empty
	}
}
