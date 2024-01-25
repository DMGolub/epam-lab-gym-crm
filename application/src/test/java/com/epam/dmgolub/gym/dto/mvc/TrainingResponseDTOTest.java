package com.epam.dmgolub.gym.dto.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrainingResponseDTOTest {

	private TrainingResponseDTO responseDTO;
	private TrainingTypeDTO type;
	private TraineeResponseDTO trainee;
	private TrainerResponseDTO trainer;

	@BeforeEach
	void setUp() {
		responseDTO = new TrainingResponseDTO();
		type = new TrainingTypeDTO(1L, "Bodybuilding");
		trainee = new TraineeResponseDTO();
		trainee.setUserName("Trainee.UserName");
		trainer = new TrainerResponseDTO();
		trainer.setUserName("Trainer.UserName");
	}

	@Test
	void traineeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setTrainee(trainee);
		assertEquals(trainee, responseDTO.getTrainee());
	}

	@Test
	void trainerGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setTrainer(trainer);
		assertEquals(trainer, responseDTO.getTrainer());
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
		final TrainingTypeDTO type = new TrainingTypeDTO(1L, "Karate");
		final TrainingResponseDTO training =
			new TrainingResponseDTO(null, null, "name", type, null, 60);
		final String expected = "TrainingResponseDTO{trainee=null, trainer=null, name='name', " +
			"type=" + type + ", date=null, duration=60}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainingTypeDTO type2 = new TrainingTypeDTO(2L, "SomeName");
		final TrainingResponseDTO responseDTO2 =
			new TrainingResponseDTO(trainee, trainer, "TestName", type, new Date(), 60);
		final TrainingResponseDTO responseDTO3 =
			new TrainingResponseDTO(null, trainer, "TestName", type, new Date(), 60);
		final TrainingResponseDTO responseDTO4 =
			new TrainingResponseDTO(trainee, null, "TestName", type, new Date(), 60);
		final TrainingResponseDTO responseDTO5 =
			new TrainingResponseDTO(trainee, trainer, "TestName2", type, new Date(), 60);
		final TrainingResponseDTO responseDTO6 =
			new TrainingResponseDTO(trainee, trainer, "TestName", type2, new Date(), 60);

		assertEquals(responseDTO2, responseDTO2);
		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2.hashCode(), responseDTO3.hashCode());
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2.hashCode(), responseDTO4.hashCode());
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2.hashCode(), responseDTO5.hashCode());
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2.hashCode(), responseDTO6.hashCode());
	}

	@Test
	void testHashcode() {
		final TrainingResponseDTO responseDTO2 =
			new TrainingResponseDTO(trainee, trainer, "TestName", type, new Date(), 60);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
