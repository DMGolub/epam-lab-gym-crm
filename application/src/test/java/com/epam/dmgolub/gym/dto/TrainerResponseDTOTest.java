package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainerResponseDTOTest {

	private TrainerResponseDTO responseDTO;
	private TrainingTypeDTO trainingType;

	@BeforeEach
	public void setUp() {
		responseDTO = new TrainerResponseDTO();
		trainingType = new TrainingTypeDTO(1L, "Bodybuilding");
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setUserId(1L);
		assertEquals(1L, responseDTO.getUserId());
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
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setId(2L);
		assertEquals(2L, responseDTO.getId());
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
			new TrainerResponseDTO(1L, "firstName", "lastName", "userName", true, 2L, type);
		final String expected = "TrainerResponseDTO{userId=1, firstName='firstName', lastName='lastName', userName='userName', " +
			"isActive=true, id=2, specialization=" + type + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEquals() {
		final TrainerResponseDTO responseDTO2 =
			new TrainerResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, trainingType);
		final TrainerResponseDTO responseDTO3 =
			new TrainerResponseDTO(2L, "FirstName", "LastName", "UserName", true, 2L, trainingType);
		final TrainerResponseDTO responseDTO4 =
			new TrainerResponseDTO(1L, "FirstName2", "LastName", "UserName", true, 2L, trainingType);
		final TrainerResponseDTO responseDTO5 =
			new TrainerResponseDTO(1L, "FirstName", "LastName2", "UserName", true, 2L, trainingType);
		final TrainerResponseDTO responseDTO6 =
			new TrainerResponseDTO(1L, "FirstName", "LastName", "UserName2", true, 2L, trainingType);
		final TrainerResponseDTO responseDTO7 =
			new TrainerResponseDTO(1L, "FirstName", "LastName", "UserName", true, 3L, trainingType);
		final TrainerResponseDTO responseDTO8 =
			new TrainerResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, new TrainingTypeDTO(3L, "Some name"));

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
	}

	@Test
	void testHashCode() {
		final TrainerResponseDTO responseDTO2 =
			new TrainerResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, trainingType);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
