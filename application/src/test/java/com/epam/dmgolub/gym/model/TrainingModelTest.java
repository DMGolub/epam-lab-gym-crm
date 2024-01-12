package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingModelTest {

	private TrainingModel responseDTO;
	private TrainingTypeModel type;
	private TraineeModel trainee;
	private TrainerModel trainer;

	@BeforeEach
	void setUp() {
		responseDTO = new TrainingModel();
		type = new TrainingTypeModel(1L, "Bodybuilding");
		trainee = new TraineeModel();
		trainer = new TrainerModel();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setId(1L);
		assertEquals(1L, responseDTO.getId());
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
		final var type = new TrainingTypeModel(1L, "Karate");
		final var training =
			new TrainingModel(1L, null, null, "name", type, null, 60);
		final String expected = "TrainingModel{id=1, trainee=null, trainer=null, name='name', " +
			"type=" + type + ", date=null, duration=60}";

		assertEquals(expected, training.toString());
	}

	@Test
	void testEquals() {
		final var type2 = new TrainingTypeModel(2L, "SomeName");
		final var responseDTO2 =
			new TrainingModel(1L, trainee, trainer, "TestName", type, new Date(), 60);
		final var responseDTO3 =
			new TrainingModel(2L, trainee, trainer, "TestName", type, new Date(), 60);
		final var responseDTO4 =
			new TrainingModel(1L, null, trainer, "TestName", type, new Date(), 60);
		final var responseDTO5 =
			new TrainingModel(1L, trainee, null, "TestName", type, new Date(), 60);
		final var responseDTO6 =
			new TrainingModel(1L, trainee, trainer, "TestName2", type, new Date(), 60);
		final var responseDTO7 =
			new TrainingModel(1L, trainee, trainer, "TestName", type2, new Date(), 60);

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
	}

	@Test
	void testHashcode() {
		final var responseDTO2 =
			new TrainingModel(1L, trainee, trainer, "TestName", type, new Date(), 60);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
