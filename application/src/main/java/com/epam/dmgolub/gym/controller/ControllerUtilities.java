package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.controller.mvc.exception.RequestIdsInvalidException;
import org.slf4j.Logger;
import org.springframework.validation.BindingResult;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.BINDING_RESULT_ERROR_MESSAGE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REQUEST_ID_INVALID_EXCEPTION_MESSAGE;

public final class ControllerUtilities {
    
    private ControllerUtilities() {
        // Empty
    }
    
    public static void logBingingResultErrors(
        final BindingResult bindingResult,
        final Logger logger,
        final String viewName
    ) {
        logger.info(BINDING_RESULT_ERROR_MESSAGE, viewName);
        for (var error : bindingResult.getAllErrors()) {
            String message = error.toString();
            logger.info(message);
        }
    }

    public static void validateRequestId(final Long pathId, final Long requestId) {
        if (!pathId.equals(requestId)) {
            throw new RequestIdsInvalidException(String.format(REQUEST_ID_INVALID_EXCEPTION_MESSAGE, requestId, pathId));
        }
    }
}
