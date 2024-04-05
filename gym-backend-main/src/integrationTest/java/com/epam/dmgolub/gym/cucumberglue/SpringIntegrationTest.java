package com.epam.dmgolub.gym.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
	"dummy.data.initialization=true",
	"spring.datasource.url=jdbc:tc:postgresql:9.6.8:///gymintegrationtest",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
@CucumberContextConfiguration
public class SpringIntegrationTest {
	// Empty
}
