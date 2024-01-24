package com.epam.dmgolub.gym.monitor;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnBean(Admin.class)
@ConditionalOnEnabledHealthIndicator("kafka")
public class KafkaHealthIndicator implements HealthIndicator {

	private static final String KAFKA = "Kafka";

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaHealthIndicator.class);

	private final Admin admin;

	public KafkaHealthIndicator(final Admin admin) {
		this.admin = admin;
	}

	@Override
	public Health health() {
		try {
			final ListTopicsResult listTopicsResult = admin.listTopics();
			final var names = listTopicsResult.names();
			long timeout = 3;
			names.get(timeout, TimeUnit.SECONDS);
			LOGGER.debug("In health - Kafka availability check successful");
			return Health.up().withDetail(KAFKA, "Available").build();
		} catch (final Exception e) {
			LOGGER.error("In health - Kafka availability check failed due to: {}", e.getMessage());
			return Health.down()
				.withDetail(KAFKA, "Unavailable")
				.withDetail("Error", e.getMessage())
				.build();
		}
	}
}
