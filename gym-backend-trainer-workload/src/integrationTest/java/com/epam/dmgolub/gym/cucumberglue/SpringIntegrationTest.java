package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.config.IntegrationTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfig.class)
@Testcontainers
@CucumberContextConfiguration
public class SpringIntegrationTest {

	private static final String MONGO_DB_IMAGE = "mongo:6.0.10";
	private static final int MONGO_DB_PORT = 27017;

	@Container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse(MONGO_DB_IMAGE))
			.withExposedPorts(MONGO_DB_PORT);

	static {
		mongoDBContainer.start();
		var mappedPort = mongoDBContainer.getMappedPort(MONGO_DB_PORT);
		System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
	}
}
