package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainerRequestDTOTest {

	private TrainerRequestDTO requestDTO;
	private TrainingTypeDTO trainingTypeDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerRequestDTO();
		trainingTypeDTO = new TrainingTypeDTO(1L, "Bodybuilding");
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setUserId(1L);
		assertEquals(1L, requestDTO.getUserId());
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
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setId(2L);
		assertEquals(2L, requestDTO.getId());
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
			new TrainerRequestDTO(1L, "firstName", "lastName", true, 2L, type);
		final String expected = "TrainerRequestDTO{userId=1, firstName='firstName', lastName='lastName', isActive=true, " +
			"id=2, specialization=" + type + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainerRequestDTO requestDTO2 =
			new TrainerRequestDTO(1L, "FirstName", "LastName", true, 2L, trainingTypeDTO);
		final TrainerRequestDTO requestDTO3 =
			new TrainerRequestDTO(2L, "FirstName", "LastName", true, 2L, trainingTypeDTO);
		final TrainerRequestDTO requestDTO4 =
			new TrainerRequestDTO(1L, "Another firstName", "LastName", true, 2L, trainingTypeDTO);
		final TrainerRequestDTO requestDTO5 =
			new TrainerRequestDTO(1L, "FirstName", "Another lastName", true, 2L, trainingTypeDTO);
		final TrainerRequestDTO requestDTO6 =
			new TrainerRequestDTO(1L, "FirstName", "LastName", true, 3L, trainingTypeDTO);

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
		assertNotEquals(requestDTO2, requestDTO6);
	}

	@Test
	void testHashCode() {
		final TrainerRequestDTO requestDTO2 =
			new TrainerRequestDTO(1L, "FirstName", "LastName", true, 2L, trainingTypeDTO);
		final TrainingRequestDTO requestDTO3 = new TrainingRequestDTO();

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertEquals(0, requestDTO3.hashCode());
	}
}
