package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.mvc.exception.RequestIdsInvalidException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.BINDING_RESULT_ERROR_MESSAGE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REQUEST_ID_INVALID_EXCEPTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

	@Nested
	class TestValidateRequestId {

		@Test
		void validateRequestId_shouldNotThrowException_whenIdsEqual() {
			try {
				final Long pathId = 1L;
				final Long requestId = 1L;
				ControllerUtilities.validateRequestId(pathId, requestId);
			} catch (final Exception e) {
				fail("No exception should be thrown");
			}
		}

		@Test
		void validateRequestId_shouldThrowRequestIdsInvalidException_whenIdsDiffer() {
			final Long pathId = 1L;
			final Long requestId = 2L;
			final Exception exception = assertThrows(RequestIdsInvalidException.class,
				() -> ControllerUtilities.validateRequestId(pathId, requestId));

			final String expectedMessage = String.format(REQUEST_ID_INVALID_EXCEPTION_MESSAGE, requestId, pathId);
			final String actualMessage = exception.getMessage();
			assertEquals(expectedMessage, actualMessage);
		}
	}
}
