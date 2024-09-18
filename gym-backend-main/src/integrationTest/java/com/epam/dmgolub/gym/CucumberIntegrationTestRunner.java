package com.epam.dmgolub.gym;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/*
 To run tests for a single endpoint from command line use the following command:
 ./gradlew clean integrationTest -PcucumberTags="@Endpoint-training-types" --info
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/",
	plugin = {"pretty"},
	glue = {"com.epam.dmgolub.gym.cucumberglue"})
public class CucumberIntegrationTestRunner {
	// Empty
}
