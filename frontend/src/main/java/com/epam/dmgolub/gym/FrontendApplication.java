package com.epam.dmgolub.gym;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(FrontendApplication.class);

	public static void main(final String[] args) {
		try {
			SpringApplication.run(FrontendApplication.class, args);
			LOGGER.debug("Frontend application run successfully");
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
