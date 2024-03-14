package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerRequestDTOTest {

	private TrainerRequestDTO requestDTO;
	private TrainingTypeDTO trainingTypeDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerRequestDTO();
		trainingTypeDTO = new TrainingTypeDTO(1L, "Bodybuilding");
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
		requestDTO.setSpecialization(trainingTypeDTO);
		assertEquals(trainingTypeDTO, requestDTO.getSpecialization());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingTypeDTO type = new TrainingTypeDTO(1L, "Karate");
		final TrainerRequestDTO trainer =
			new TrainerRequestDTO("firstName", "lastName", "firstName.lastName", true, type);
		final String expected = "TrainerRequestDTO{firstName='firstName', lastName='lastName', " +
			"userName='firstName.lastName', isActive=true, specialization=" + type + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainerRequestDTO requestDTO2 =
			new TrainerRequestDTO("FirstName", "LastName", "firstName.lastName", true, trainingTypeDTO);
		final TrainerRequestDTO requestDTO3 =
			new TrainerRequestDTO("Another firstName", "LastName", "firstName.lastName", true, trainingTypeDTO);
		final TrainerRequestDTO requestDTO4 =
			new TrainerRequestDTO("FirstName", "Another lastName", "firstName.lastName", true, trainingTypeDTO);
		final TrainerRequestDTO requestDTO5 =
			new TrainerRequestDTO("FirstName", "LastName", "firstName.lastName2", true, trainingTypeDTO);

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
		assertNotEquals(requestDTO2.hashCode(), requestDTO5.hashCode());
	}
}
