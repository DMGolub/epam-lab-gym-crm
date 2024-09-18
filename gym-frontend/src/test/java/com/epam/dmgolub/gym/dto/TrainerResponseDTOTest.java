package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerResponseDTOTest {

	private TrainerResponseDTO responseDTO;
	private TrainingTypeDTO trainingType;

	@BeforeEach
	public void setUp() {
		responseDTO = new TrainerResponseDTO();
		trainingType = new TrainingTypeDTO(1L, "Bodybuilding");
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setFirstName("FirstName");
		assertEquals("FirstName", responseDTO.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setLastName("LastName");
		assertEquals("LastName", responseDTO.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setUserName("UserName");
		assertEquals("UserName", responseDTO.getUserName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setActive(true);
		assertTrue(responseDTO.isActive());
	}

	@Test
	void specializationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setSpecialization(trainingType);
		assertEquals(trainingType, responseDTO.getSpecialization());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingTypeDTO type = new TrainingTypeDTO(1L, "Karate");
		final TrainerResponseDTO trainer =
			new TrainerResponseDTO("firstName", "lastName", "userName", true, type);
		final String expected = "TrainerResponseDTO{firstName='firstName', lastName='lastName', userName='userName', " +
			"isActive=true, specialization=" + type + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEquals() {
		final TrainerResponseDTO responseDTO2 =
			new TrainerResponseDTO("FirstName", "LastName", "UserName", true, trainingType);
		final TrainerResponseDTO responseDTO3 =
			new TrainerResponseDTO("FirstName2", "LastName", "UserName", true, trainingType);
		final TrainerResponseDTO responseDTO4 =
			new TrainerResponseDTO("FirstName", "LastName2", "UserName", true, trainingType);
		final TrainerResponseDTO responseDTO5 =
			new TrainerResponseDTO("FirstName", "LastName", "UserName2", true, trainingType);
		final TrainerResponseDTO responseDTO6 =
			new TrainerResponseDTO("FirstName", "LastName", "UserName", true, new TrainingTypeDTO(3L, "Some name"));

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
	}

	@Test
	void testHashCode() {
		final TrainerResponseDTO responseDTO2 =
			new TrainerResponseDTO("FirstName", "LastName", "UserName", true, trainingType);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
