package com.epam.dmgolub.gym.controller.utility;

import org.slf4j.Logger;
import org.springframework.validation.BindingResult;

import static com.epam.dmgolub.gym.controller.constant.Constants.BINDING_RESULT_ERROR_MESSAGE;

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
}
