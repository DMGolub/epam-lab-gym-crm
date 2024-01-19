package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeModelTest {

	private TraineeModel trainee;

	@BeforeEach
	public void setUp() {
		trainee = new TraineeModel();
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setUserId(1L);
		assertEquals(1L, trainee.getUserId());
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setFirstName("FirstName");
		assertEquals("FirstName", trainee.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setLastName("LastName");
		assertEquals("LastName", trainee.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setUserName("UserName");
		assertEquals("UserName", trainee.getUserName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setActive(true);
		assertTrue(trainee.isActive());
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setId(2L);
		assertEquals(2L, trainee.getId());
	}

	@Test
	void dateOfBirthGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		Date date = new Date();
		trainee.setDateOfBirth(date);
		assertEquals(date, trainee.getDateOfBirth());
	}

	@Test
	void addressGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainee.setAddress("Address");
		assertEquals("Address", trainee.getAddress());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TraineeModel trainee =
			new TraineeModel(1L, "firstName", "lastName", "userName", "Password", true, 2L, null, "address", new ArrayList<>());
		final String expected = "TraineeModel{userId=1, firstName='firstName', lastName='lastName', " +
			"userName='userName', isActive=true, id=2, dateOfBirth=null, address='address', trainers=[]}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final var trainer = new TraineeModel.Trainer();
		final var responseDTO2 =
			new TraineeModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, new Date(), "Address", new ArrayList<>());
		final var responseDTO3 =
			new TraineeModel(2L, "FirstName", "LastName", "UserName", "Password", true, 2L, new Date(), "Address", new ArrayList<>());
		final var responseDTO4 =
			new TraineeModel(1L, "FirstName2", "LastName", "UserName", "Password", true, 2L, new Date(), "Address", new ArrayList<>());
		final var responseDTO5 =
			new TraineeModel(1L, "FirstName", "LastName2", "UserName", "Password", true, 2L, new Date(), "Address", new ArrayList<>());
		final var responseDTO6 =
			new TraineeModel(1L, "FirstName", "LastName", "UserName2", "Password", true, 2L, new Date(), "Address", new ArrayList<>());
		final var responseDTO7 =
			new TraineeModel(1L, "FirstName", "LastName", "UserName", "Password", true, 3L, new Date(), "Address", new ArrayList<>());
		final var responseDTO8 =
			new TraineeModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, new Date(), "Address2", new ArrayList<>());
		final var responseDTO9 =
			new TraineeModel(1L, "FirstName", "LastName", "UserName", "Password", true, 2L, new Date(), "Address", List.of(trainer));

		assertEquals(responseDTO2, responseDTO2);
		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(trainee, responseDTO2);
		assertNotEquals(trainee.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
		assertNotEquals(responseDTO2, responseDTO9);
	}
}
