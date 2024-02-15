package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerModelTest {

	private TrainerModel trainer;
	private TrainingTypeModel trainingType;

	@BeforeEach
	public void setUp() {
		trainer = new TrainerModel();
		trainingType = new TrainingTypeModel(1L, "Bodybuilding");
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setUserId(1L);
		assertEquals(1L, trainer.getUserId());
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setFirstName("FirstName");
		assertEquals("FirstName", trainer.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setLastName("LastName");
		assertEquals("LastName", trainer.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setUserName("UserName");
		assertEquals("UserName", trainer.getUserName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setActive(true);
		assertTrue(trainer.isActive());
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setId(2L);
		assertEquals(2L, trainer.getId());
	}

	@Test
	void specializationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainer.setSpecialization(trainingType);
		assertEquals(trainingType, trainer.getSpecialization());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final var type = new TrainingTypeModel(1L, "Karate");
		final TrainerModel trainer =
			new TrainerModel(1L, "firstName", "lastName", "userName", "Password", true, 2L, type);
		final String expected = "TrainerModel{userId=1, firstName='firstName', lastName='lastName', userName='userName', " +
			"isActive=true, id=2, specialization=" + type + "}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEquals() {
		final var responseDTO2 =
			new TrainerModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, trainingType);
		final var responseDTO3 =
			new TrainerModel(2L, "FirstName", "LastName", "UserName", "Password", true, 2L, trainingType);
		final var responseDTO4 =
			new TrainerModel(1L, "FirstName2", "LastName", "UserName", "Password", true, 2L, trainingType);
		final var responseDTO5 =
			new TrainerModel(1L, "FirstName", "LastName2", "UserName", "Password", true, 2L, trainingType);
		final var responseDTO6 =
			new TrainerModel(1L, "FirstName", "LastName", "UserName2", "Password", true, 2L, trainingType);
		final var responseDTO7 =
			new TrainerModel(1L, "FirstName", "LastName", "UserName", "Password", true, 3L, trainingType);
		final var responseDTO8 =
			new TrainerModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, new TrainingTypeModel(3L, "Some name"));

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(trainer, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
	}

	@Test
	void testHashCode() {
		final var responseDTO2 =
			new TrainerModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, trainingType);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(trainer.hashCode(), responseDTO2.hashCode());
	}

	@Nested
	class TraineeTest {

		@Test
		void toString_shouldReturnExpectedValue_whenInvoked() {
			final var trainee =
				new TrainerModel.Trainee("userName", "firstName", "lastName");
			final String expected = "Trainee{userName='userName', firstName='firstName', lastName='lastName'}";

			assertEquals(expected, trainee.toString());
		}

		@Test
		void testEqualsAndHashCode() {
			final var trainee =
				new TrainerModel.Trainee("userName", "firstName", "lastName");
			final var trainee2 =
				new TrainerModel.Trainee("userName2", "firstName", "lastName");
			final var trainee3 =
				new TrainerModel.Trainee("userName", "firstName3", "lastName");
			final var trainee4 =
				new TrainerModel.Trainee("userName", "firstName", "lastName4");

			assertEquals(trainee, trainee);
			assertEquals(trainee.hashCode(), trainee.hashCode());
			assertNotEquals(trainee, trainee2);
			assertNotEquals(trainee.hashCode(), trainee2.hashCode());
			assertNotEquals(trainee, trainee3);
			assertNotEquals(trainee.hashCode(), trainee3.hashCode());
			assertNotEquals(trainee, trainee4);
			assertNotEquals(trainee.hashCode(), trainee4.hashCode());
		}
	}
}
