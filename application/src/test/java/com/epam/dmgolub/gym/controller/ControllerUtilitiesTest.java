package com.epam.dmgolub.gym.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.BINDING_RESULT_ERROR_MESSAGE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerUtilitiesTest {

	@Test
	void logBindingResultErrors_shouldLogException_whenBindingResultHasErrors() {
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		final Logger logger = Mockito.mock(Logger.class);

		final List<ObjectError> errorList = new ArrayList<>();
		errorList.add(new ObjectError("object", "error message"));
		when(bindingResult.getAllErrors()).thenReturn(errorList);

		final String viewName = "view";
		ControllerUtilities.logBingingResultErrors(bindingResult, logger, viewName);

		verify(logger).info(BINDING_RESULT_ERROR_MESSAGE, viewName);
	}
}
