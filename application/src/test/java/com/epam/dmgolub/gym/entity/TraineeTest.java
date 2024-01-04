package com.epam.dmgolub.gym.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeTest {

	private Trainee trainee;

	@BeforeEach
	void setUp() {
		this.trainee = new Trainee();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long traineeId = 1L;
		trainee.setId(traineeId);
		assertEquals(traineeId, trainee.getId());
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long userId = 2L;
		trainee.setUserId(userId);
		assertEquals(userId, trainee.getUserId());
	}

	@Test
	void dateOfBirthGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date birthDate = new Date();
		trainee.setDateOfBirth(birthDate);
		assertEquals(birthDate, trainee.getDateOfBirth());
	}

	@Test
	void addressGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String address = "New York";
		trainee.setAddress(address);
		assertEquals(address, trainee.getAddress());
	}

	@Test
	void testEquals() {
		final Trainee traineeOne =
			new Trainee(1L, "John", "Doe", "John.Doe", "password", true, 2L, new Date(), "New York");
		final Trainee traineeTwo =
			new Trainee(2L, "Jane", "Doe", "Jane.Doe", "password", true, 4L, new Date(), "New York");
		final Trainee traineeThree =
			new Trainee(2L, "Jane", "Doe", "Jane.Doe", "password", true, 4L, Date.from(Instant.ofEpochMilli(1000)), "New York");
		final Trainee traineeFour =
			new Trainee(2L, "Jane", "Doe", "Jane.Doe", "password", true, 4L, new Date(), "Old York");

		assertEquals(traineeTwo, traineeTwo);
		assertNotEquals(traineeOne, traineeTwo);
		assertNotEquals(traineeTwo, traineeThree);
		assertNotEquals(traineeTwo, traineeFour);
	}

	@Test
	void testHashCode() {
		final Trainee traineeOne =
			new Trainee(1L, "John", "Doe", "John.Doe", "password", true, 2L, new Date(), "New York");
		final Trainee traineeTwo =
			new Trainee(2L, "Jane", "Doe", "Jane.Doe", "password", true, 4L, new Date(), "New York");
		final Trainee traineeThree =
			new Trainee();

		assertEquals(traineeTwo.hashCode(), traineeTwo.hashCode());
		assertNotEquals(traineeOne.hashCode(), traineeTwo.hashCode());
		assertEquals(0, traineeThree.hashCode());
	}
}
