package com.epam.dmgolub.gym.cucumberglue;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.text.SimpleDateFormat;

public abstract class AbstractStepDefinition {

	protected ResponseEntity<?> lastResponse;

	@LocalServerPort
	protected String port;
	protected final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
	protected final TestRestTemplate restTemplate;

	protected AbstractStepDefinition(final TestRestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
