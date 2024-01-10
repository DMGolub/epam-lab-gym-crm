package com.epam.dmgolub.gym.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

	private Trainer trainer;

	@BeforeEach
	void setUp() {
		this.trainer = new Trainer();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long trainerId = 1L;
		trainer.setId(trainerId);
		assertEquals(trainerId, trainer.getId());
	}

	@Test
	void userGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final User user = new User();
		user.setId(2L);
		trainer.setUser(user);

		assertEquals(user, trainer.getUser());
	}

	@Test
	void specializationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final TrainingType type = new TrainingType(1L, "Bodybuilding");
		trainer.setSpecialization(type);
		assertEquals(type, trainer.getSpecialization());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingType type = new TrainingType(1L, "Bodybuilding");
		final Trainer trainerOne =
			new Trainer(1L, "John", "Doe", "John.Doe", "password", true, 2L, type, new ArrayList<>());
		final String expected = "Trainer{id=1, firstName='John', lastName='Doe', userName='John.Doe', isActive=true, " +
			"userId=2, specialization=" + type + "}";

		assertEquals(expected, trainerOne.toString());
	}

	@Test
	void testEquals() {
		final Trainer trainerOne = new Trainer(
			1L,
			"John",
			"Doe",
			"John.Doe",
			"password",
			true,
			2L,
			new TrainingType(1L, "Bodybuilding"),
			new ArrayList<>()
		);
		final Trainer trainerTwo = new Trainer(
			2L,
			"Jane",
			"Doe",
			"Jane.Doe",
			"password",
			true,
			4L,
			new TrainingType(1L, "Bodybuilding"),
			new ArrayList<>()
		);
		final Trainer trainerThree = new Trainer(
			2L,
			"Jane",
			"Doe",
			"Jane.Doe",
			"password",
			true,
			4L,
			new TrainingType(1L, "Martial arts"),
			new ArrayList<>()
		);

		assertEquals(trainerTwo, trainerTwo);
		assertNotEquals(trainerOne, trainerTwo);
		assertNotEquals(new Object(), trainerTwo);
		assertNotEquals(trainerTwo, trainerThree);
	}

	@Test
	void testHashCode() {
		final Trainer trainerOne = new Trainer(
			1L,
			"John",
			"Doe",
			"John.Doe",
			"password",
			true,
			2L,
			new TrainingType(1L, "Bodybuilding"),
			new ArrayList<>()
		);
		final Trainer trainerTwo = new Trainer(
			2L,
			"Jane",
			"Doe",
			"Jane.Doe",
			"password",
			true,
			4L,
			new TrainingType(1L, "Bodybuilding"),
			new ArrayList<>()
		);
		final Trainer trainerThree = new Trainer();

		assertEquals(trainerTwo.hashCode(), trainerTwo.hashCode());
		assertNotEquals(trainerOne.hashCode(), trainerTwo.hashCode());
		assertEquals(0, trainerThree.hashCode());
	}
}
