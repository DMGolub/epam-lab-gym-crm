package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrainingRequestDTOTest {

	private TrainingRequestDTO requestDTO;
	private TrainingTypeDTO type;

	@BeforeEach
	void setUp() {
		requestDTO = new TrainingRequestDTO();
		type = new TrainingTypeDTO(1L, "Bodybuilding");
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setId(1L);
		assertEquals(1L, requestDTO.getId());
	}

	@Test
	void traineeIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setTraineeId(1L);
		assertEquals(1L, requestDTO.getTraineeId());
	}

	@Test
	void trainerIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setTrainerId(1L);
		assertEquals(1L, requestDTO.getTrainerId());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setName("Training1");
		assertEquals("Training1", requestDTO.getName());
	}

	@Test
	void typeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setType(type);
		assertEquals(type, requestDTO.getType());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		Date date = new Date();
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
		final TrainingTypeDTO type = new TrainingTypeDTO(1L, "Karate");
		final TrainingRequestDTO training =
			new TrainingRequestDTO(1L, 2L, 3L, "name", type, null, 60);
		final String expected = "TrainingRequestDTO{id=1, traineeId=2, trainerId=3, name='name', " +
			"type=" + type + ", date=null, duration=60}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainingRequestDTO requestDTO2 =
			new TrainingRequestDTO(1L, 2L, 3L, "Training1", type, new Date(), 60);

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
