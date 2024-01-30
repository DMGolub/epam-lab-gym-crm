package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerTrainingResponseDTOTest {

	private TrainerTrainingResponseDTO responseDTO;
	private TrainingTypeDTO type;

	@BeforeEach
	void setUp() {
		responseDTO = new TrainerTrainingResponseDTO();
		type = new TrainingTypeDTO("Bodybuilding", 1L);
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "Trainee.UserName";
		responseDTO.setTraineeUserName(userName);
		assertEquals(userName, responseDTO.getTraineeUserName());
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
			new TrainerTrainingResponseDTO("name", null, type, 60, "Trainee.UserName");
		final String expected = "TrainerTrainingResponseDTO{name='name', date=null, type=" + type + ", duration=60, " +
			"traineeUserName='Trainee.UserName'}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEquals() {
		final var type2 = new TrainingTypeDTO("SomeName", 2L);
		final var responseDTO2 =
			new TrainerTrainingResponseDTO("TestName", new Date(), type, 60, "Trainee.UserName");
		final var responseDTO3 =
			new TrainerTrainingResponseDTO("TestName", new Date(), type, 60, "Another.UserName");
		final var responseDTO4 =
			new TrainerTrainingResponseDTO("TestName2", new Date(), type, 60, "Trainee.UserName");
		final var responseDTO5 =
			new TrainerTrainingResponseDTO("TestName", new Date(), type2, 60, "Trainee.UserName");

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
	}

	@Test
	void testHashcode() {
		final var responseDTO2 =
			new TrainerTrainingResponseDTO("TestName", new Date(), type, 60, "Trainee.UserName");

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
