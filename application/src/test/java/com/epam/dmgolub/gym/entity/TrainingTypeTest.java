package com.epam.dmgolub.gym.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeTest {

	private TrainingType trainingType;

	@BeforeEach
	void setUp() {
		this.trainingType = new TrainingType();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long trainingTypeId = 1L;
		trainingType.setId(trainingTypeId);
		assertEquals(trainingTypeId, trainingType.getId());
	}

	@Test
	void nameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String trainingTypeName = "Martial Arts";
		trainingType.setName(trainingTypeName);
		assertEquals(trainingTypeName, trainingType.getName());
	}


	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainingType type = new TrainingType(2L, "Martial arts");
		final String expected = "TrainingType{id=2, name='Martial arts'}";

		assertEquals(expected, type.toString());
	}

	@Test
	void testEquals() {
		final TrainingType trainingTypeOne = new TrainingType(1L, "Yoga");
		final TrainingType trainingTypeTwo = new TrainingType(2L, "Martial arts");
		final TrainingType trainingTypeThree = new TrainingType(1L, "Martial arts");

		assertEquals(trainingTypeTwo, trainingTypeTwo);
		assertNotEquals(trainingTypeOne, trainingTypeTwo);
		assertNotEquals(trainingTypeOne, trainingTypeThree);
		assertNotEquals(new Object(), trainingTypeOne);
	}

	@Test
	void testHashCode() {
		final TrainingType trainingTypeOne = new TrainingType(1L, "Yoga");
		final TrainingType trainingTypeTwo = new TrainingType(2L, "Martial arts");
		final TrainingType trainingTypeThree = new TrainingType();

		assertEquals(trainingTypeTwo.hashCode(), trainingTypeTwo.hashCode());
		assertNotEquals(trainingTypeOne.hashCode(), trainingTypeTwo.hashCode());
		assertEquals(0, trainingTypeThree.hashCode());
	}
}
