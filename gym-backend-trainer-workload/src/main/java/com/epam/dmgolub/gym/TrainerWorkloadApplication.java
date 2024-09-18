package com.epam.dmgolub.gym;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TrainerWorkloadApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadApplication.class);

	public static void main(final String[] args) {
		try {
			SpringApplication.run(TrainerWorkloadApplication.class, args);
			LOGGER.debug("Application run successfully");
		} catch (final Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
}
