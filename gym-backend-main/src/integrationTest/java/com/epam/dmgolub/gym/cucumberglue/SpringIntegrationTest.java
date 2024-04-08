package com.epam.dmgolub.gym.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
	"dummy.data.initialization=true",
	"spring.datasource.url=jdbc:tc:postgresql:9.6.8:///gymintegrationtest",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
@Testcontainers
@CucumberContextConfiguration
public class SpringIntegrationTest {

	private static final String ACTIVE_MQ_IMAGE = "apache/activemq-classic:5.18.3";
	private static final int ACTIVE_MQ_PORT = 61616;

	@Container
	static ActiveMQContainer activeMQContainer = new ActiveMQContainer(DockerImageName.parse(ACTIVE_MQ_IMAGE))
		.withExposedPorts(ACTIVE_MQ_PORT);

	@DynamicPropertySource
	static void setProperties(final DynamicPropertyRegistry registry) {
		activeMQContainer.start();
		registry.add("spring.activemq.broker-url",
			() -> "tcp://localhost:" + activeMQContainer.getMappedPort(ACTIVE_MQ_PORT));
	}
}
