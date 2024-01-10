package com.epam.dmgolub.gym.validation.annotation;

import com.epam.dmgolub.gym.validation.validator.MinimumAgeLimitValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = MinimumAgeLimitValidator.class)
public @interface MinimumAgeLimit {

	int value();
	String message();
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
