package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTypeModelTest {

	private TrainingTypeModel trainingType;

	@BeforeEach
	void setUp() {
		this.trainingType = new TrainingTypeModel();
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
		final TrainingTypeModel type = new TrainingTypeModel(2L, "Martial arts");
		final String expected = "TrainingTypeModel{id=2, name='Martial arts'}";

		assertEquals(expected, type.toString());
	}

	@Test
	void testEquals() {
		final TrainingTypeModel trainingTypeOne = new TrainingTypeModel(1L, "Yoga");
		final TrainingTypeModel trainingTypeTwo = new TrainingTypeModel(2L, "Martial arts");
		final TrainingTypeModel trainingTypeThree = new TrainingTypeModel(1L, "Martial arts");

		assertEquals(trainingTypeTwo, trainingTypeTwo);
		assertNotEquals(trainingTypeOne, trainingTypeTwo);
		assertNotEquals(trainingTypeOne, trainingTypeThree);
		assertNotEquals(new Object(), trainingTypeOne);
	}

	@Test
	void testHashCode() {
		final TrainingTypeModel trainingTypeOne = new TrainingTypeModel(1L, "Yoga");
		final TrainingTypeModel trainingTypeTwo = new TrainingTypeModel(2L, "Martial arts");
		final TrainingTypeModel trainingTypeThree = new TrainingTypeModel();

		assertEquals(trainingTypeTwo.hashCode(), trainingTypeTwo.hashCode());
		assertNotEquals(trainingTypeOne.hashCode(), trainingTypeTwo.hashCode());
		assertEquals(0, trainingTypeThree.hashCode());
	}
}
