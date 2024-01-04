package com.epam.dmgolub.gym.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainingTest {

	private Training training;

	@BeforeEach
	void setUp() {
		this.training = new Training();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long trainingId = 1L;
		training.setId(trainingId);
		assertEquals(trainingId, training.getId());
	}

	@Test
	void traineeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Trainee trainee = new Trainee();
		training.setTrainee(trainee);
		assertEquals(trainee, training.getTrainee());
	}

	@Test
	void trainerGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Trainer trainer = new Trainer();
		training.setTrainer(trainer);
		assertEquals(trainer, training.getTrainer());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String trainingName = "Martial Arts";
		training.setName(trainingName);
		assertEquals(trainingName, training.getName());
	}

	@Test
	void typeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final TrainingType type = new TrainingType(1L, "Martial Arts");
		training.setType(type);
		assertEquals(type, training.getType());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		Date trainingDate = new Date();
		training.setDate(trainingDate);
		assertEquals(trainingDate, training.getDate());
	}

	@Test
	void durationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		int duration = 60;
		training.setDuration(duration);
		assertEquals(duration, training.getDuration());
	}

	@Test
	void testEquals() {
		final Training trainingOne =
			new Training(1L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), new Date(), 60);
		final Training trainingTwo =
			new Training(2L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), new Date(), 90);
		final Training trainingThree =
			new Training(3L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), new Date(), 90);
		final Training trainingFour =
			new Training(3L, new Trainee(), new Trainer(), "Karate", new TrainingType(1L, "Martial Arts"), new Date(), 90);
		final Training trainingFive =
			new Training(1L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), Date.from(Instant.ofEpochMilli(1000)), 60);

		assertEquals(trainingTwo, trainingTwo);
		assertNotEquals(trainingOne, trainingTwo);
		assertNotEquals(trainingTwo, trainingThree);
		assertNotEquals(trainingThree, trainingFour);
		assertNotEquals(trainingOne, trainingFive);
	}

	@Test
	void testHashCode() {
		final Training trainingOne =
			new Training(1L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), new Date(), 60);
		final Training trainingTwo =
			new Training(2L, new Trainee(), new Trainer(), "Martial Arts", new TrainingType(1L, "Martial Arts"), new Date(), 90);
		final Training trainingThree = new Training();

		assertEquals(trainingTwo.hashCode(), trainingTwo.hashCode());
		assertNotEquals(trainingOne.hashCode(), trainingTwo.hashCode());
		assertEquals(0, trainingThree.hashCode());
	}
}
