package com.epam.dmgolub.gym.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("dbSchema")
public class DbSchemaHealthIndicator implements HealthIndicator {

	private static final String TABLE_COUNT_QUERY =
		"SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ";
	private static final String TRAINEE_TABLE_NAME = "trainee";
	private static final String TRAINER_TABLE_NAME = "trainer";
	private static final String TRAINER_TO_TRAINEE_TABLE_NAME = "trainer_to_trainee";
	private static final String TRAINING_TABLE_NAME = "training";
	private static final String TRAINING_TYPE_TABLE_NAME = "training_type";
	private static final String USER_TABLE_NAME = "users";
	private static final String DATABASE_SCHEMA = "Database schema";

	private static final Logger LOGGER = LoggerFactory.getLogger(DbSchemaHealthIndicator.class);

	private final JdbcTemplate jdbcTemplate;

	public DbSchemaHealthIndicator(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Health health() {
		try {
			if (isSchemaValid()) {
				LOGGER.debug("In health - Database schema health check successful");
				return Health.up().withDetail(DATABASE_SCHEMA, "Valid").build();
			}
			LOGGER.error("In health - Database schema is invalid");
			return Health.down().withDetail(DATABASE_SCHEMA, "Invalid").build();
		} catch (final Exception e) {
			LOGGER.error("In health - Database schema health check failed due to: {}", e.getMessage());
			return Health.down()
				.withDetail(DATABASE_SCHEMA, "Unavailable")
				.withDetail("Error", e.getMessage())
				.build();
		}
	}

	private boolean isSchemaValid() {
		return isTablePresent(TRAINEE_TABLE_NAME) &&
			isTablePresent(TRAINER_TABLE_NAME) &&
			isTablePresent(TRAINER_TO_TRAINEE_TABLE_NAME) &&
			isTablePresent(TRAINING_TABLE_NAME) &&
			isTablePresent(TRAINING_TYPE_TABLE_NAME) &&
			isTablePresent(USER_TABLE_NAME);
	}

	private boolean isTablePresent(final String tableName) {
		return Integer.valueOf(1).equals(executeTableCountQuery(tableName));
	}

	private Integer executeTableCountQuery(final String tableName) {
		return jdbcTemplate.queryForObject(TABLE_COUNT_QUERY + "'" + tableName + "'", Integer.class);
	}
}
