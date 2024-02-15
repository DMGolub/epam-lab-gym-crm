package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerCreateRequestDTOTest {

	private TrainerCreateRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerCreateRequestDTO();
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setFirstName("FirstName");
		assertEquals("FirstName", requestDTO.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setLastName("LastName");
		assertEquals("LastName", requestDTO.getLastName());
	}

	@Test
	void specializationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String typeReference = "api/v1/training-types/1";
		requestDTO.setSpecialization(typeReference);
		assertEquals(typeReference, requestDTO.getSpecialization());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final String typeReference = "api/v1/training-types/1";
		final TrainerCreateRequestDTO trainer = new TrainerCreateRequestDTO("firstName", "lastName", typeReference);
		final String expected =
			"TrainerCreateRequestDTO{firstName='firstName', lastName='lastName', specialization=" + typeReference + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final String typeReference = "api/v1/training-types/1";
		final var requestDTO2 = new TrainerCreateRequestDTO("FirstName", "LastName", typeReference);
		final var requestDTO3 = new TrainerCreateRequestDTO("Another firstName", "LastName", typeReference);
		final var requestDTO4 = new TrainerCreateRequestDTO("FirstName", "Another lastName", typeReference);
		final var requestDTO5 = new TrainerCreateRequestDTO("FirstName", "LastName", "api/v1/training-types/2");

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
	}

	@Test
	void testHashCode() {
		final String typeReference = "api/v1/training-types/1";
		final var requestDTO2 = new TrainerCreateRequestDTO("FirstName", "LastName", typeReference);
		final var requestDTO3 = new TrainerCreateRequestDTO();

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertEquals(0, requestDTO3.hashCode());
	}
}
