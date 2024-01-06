package com.epam.dmgolub.gym.validation.validator;

import com.epam.dmgolub.gym.validation.annotation.MinimumAgeLimit;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class MinimumAgeLimitValidator implements ConstraintValidator<MinimumAgeLimit, Date> {

	private int minimumAgeLimit;

	@Override
	public void initialize(final MinimumAgeLimit constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		this.minimumAgeLimit = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(final Date dateOfBirth, final ConstraintValidatorContext context) {
		if (dateOfBirth == null) {
			return false;
		}

		final Calendar dateOfBirthCalendar = Calendar.getInstance();
		dateOfBirthCalendar.setTime(dateOfBirth);

		final Calendar limitCalendar = Calendar.getInstance();
		limitCalendar.add(Calendar.YEAR, -minimumAgeLimit);

		return limitCalendar.after(dateOfBirthCalendar);
	}
}
