package com.epam.dmgolub.gym.dto.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeTrainingResponseDTOTest {

	private TraineeTrainingResponseDTO responseDTO;
	private TrainingTypeDTO type;

	@BeforeEach
	void setUp() {
		responseDTO = new TraineeTrainingResponseDTO();
		type = new TrainingTypeDTO("Bodybuilding", 1L);
	}

	@Test
	void trainerUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "Trainer.UserName";
		responseDTO.setTrainerUserName(userName);
		assertEquals(userName, responseDTO.getTrainerUserName());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setName("TestName");
		assertEquals("TestName", responseDTO.getName());
	}

	@Test
	void typeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setType(type);
		assertEquals(type, responseDTO.getType());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		Date date = new Date();
		responseDTO.setDate(date);
		assertEquals(date, responseDTO.getDate());
	}

	@Test
	void durationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setDuration(60);
		assertEquals(60, responseDTO.getDuration());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingTypeDTO type = new TrainingTypeDTO("Karate", 1L);
		final var training =
			new TraineeTrainingResponseDTO("name", null, type, 60, "Trainer.UserName");
		final String expected = "TraineeTrainingResponseDTO{name='name', date=null, type=" + type + ", duration=60, " +
			"trainerUserName='Trainer.UserName'}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEquals() {
		final var type2 = new TrainingTypeDTO("SomeName", 2L);
		final var responseDTO2 =
			new TraineeTrainingResponseDTO("TestName", new Date(), type, 60, "Trainer.UserName");
		final var responseDTO4 =
			new TraineeTrainingResponseDTO("TestName", new Date(), type, 60, "Another.UserName");
		final var responseDTO6 =
			new TraineeTrainingResponseDTO("TestName2", new Date(), type, 60, "Trainer.UserName");
		final var responseDTO7 =
			new TraineeTrainingResponseDTO("TestName", new Date(), type2, 60, "Trainer.UserName");

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
	}

	@Test
	void testHashcode() {
		final var responseDTO2 =
			new TraineeTrainingResponseDTO("TestName", new Date(), type, 60, "Trainer.UserName");

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
