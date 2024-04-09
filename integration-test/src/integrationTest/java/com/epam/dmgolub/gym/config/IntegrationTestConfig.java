package com.epam.dmgolub.gym.config;

import com.epam.dmgolub.gym.cucumberglue.SpringIntegrationTest;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import static com.epam.dmgolub.gym.cucumberglue.SpringIntegrationTest.MONGO_DB_PORT;

@TestConfiguration
public class IntegrationTestConfig {

	@Bean
	public TestRestTemplate testRestTemplate() {
		return new TestRestTemplate();
	}

	@Bean
	public MongoClient mongoClient() {
		int mongoPort = SpringIntegrationTest.mongoDBContainer.getMappedPort(MONGO_DB_PORT);
		return MongoClients.create("mongodb://localhost:" + mongoPort);
	}

	@Bean
	public MongoTemplate mongoTemplate(final MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, "test");
	}
}
