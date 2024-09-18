package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingCreateRequestDTOTest {

	private TrainingCreateRequestDTO requestDTO;

	@BeforeEach
	void setUp() {
		requestDTO = new TrainingCreateRequestDTO();
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		requestDTO.setTraineeUserName(userName);
		assertEquals(userName, requestDTO.getTraineeUserName());
	}

	@Test
	void trainerIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		requestDTO.setTrainerUserName(userName);
		assertEquals(userName, requestDTO.getTrainerUserName());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setName("Training1");
		assertEquals("Training1", requestDTO.getName());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		requestDTO.setDate(date);
		assertEquals(date, requestDTO.getDate());
	}

	@Test
	void durationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setDuration(60);
		assertEquals(60, requestDTO.getDuration());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final var training =
			new TrainingCreateRequestDTO("User.Name1", "User.Name2", "name", null, 60);
		final String expected = "TrainingCreateRequestDTO{traineeUserName='User.Name1', trainerUserName='User.Name2', name='name', date=null, duration=60}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEquals() {
		final var requestDTO2 =
			new TrainingCreateRequestDTO("User.Name2", "User.Name3", "Training1", new Date(), 60);
		final var requestDTO3 =
			new TrainingCreateRequestDTO("User.Name3", "User.Name3", "Training1", new Date(), 60);
		final var requestDTO4 =
			new TrainingCreateRequestDTO("User.Name2", "User.Name4", "Training1", new Date(), 60);
		final var requestDTO5 =
			new TrainingCreateRequestDTO("User.Name2", "User.Name3", "Training2", new Date(), 60);

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(null, requestDTO);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
	}

	@Test
	void testHashCode() {
		final var requestDTO2 =
			new TrainingCreateRequestDTO("User.Name2", "User.Name3", "Training1", new Date(), 60);

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
