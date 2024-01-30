package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerUpdateRequestDTOTest {

	private TrainerUpdateRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerUpdateRequestDTO();
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
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setActive(true);
		assertTrue(requestDTO.isActive());
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
		final var trainer = new TrainerUpdateRequestDTO("firstName.lastName", "firstName", "lastName", typeReference, true);
		final String expected = "TrainerUpdateRequestDTO{userName='firstName.lastName', firstName='firstName', " +
			"lastName='lastName', specialization=" + typeReference + ", isActive=true}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final String typeReference = "api/v1/training-types/1";
		final var requestDTO2 =
			new TrainerUpdateRequestDTO("firstName.lastName", "FirstName", "LastName", typeReference, true);
		final var requestDTO3 =
			new TrainerUpdateRequestDTO("firstName.lastName", "Another firstName", "LastName", typeReference, true);
		final var requestDTO4 =
			new TrainerUpdateRequestDTO("firstName.lastName", "FirstName", "Another lastName", typeReference, true);
		final var requestDTO5 =
			new TrainerUpdateRequestDTO("firstName.lastName2", "FirstName", "LastName", typeReference, true);
		final var requestDTO6 =
			new TrainerUpdateRequestDTO("firstName.lastName", "FirstName", "LastName", "api/v1/training-types/2", true);

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
		assertNotEquals(requestDTO2, requestDTO6);
	}

	@Test
	void testHashCode() {
		final String typeReference = "api/v1/training-types/1";
		final var requestDTO2 =
			new TrainerUpdateRequestDTO("FirstName.LastName", "FirstName", "LastName", typeReference, true);
		final var requestDTO3 = new TrainerUpdateRequestDTO();

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertEquals(0, requestDTO3.hashCode());
	}
}
