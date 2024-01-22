package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserModelTest {

	private UserModel user;

	@BeforeEach
	public void setUp() {
		user = new UserModel();
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Long id = 1L;
		user.setId(id);
		assertEquals(id, user.getId());
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String firstName = "FirstName";
		user.setFirstName(firstName);
		assertEquals(firstName, user.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String lastName = "LastName";
		user.setLastName(lastName);
		assertEquals(lastName, user.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		user.setUserName(userName);
		assertEquals(userName, user.getUserName());
	}

	@Test
	void passwordGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String password = "Password";
		user.setPassword(password);
		assertEquals(password, user.getPassword());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		user.setActive(true);
		assertTrue(user.isActive());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final var user2 = new UserModel(1L, "firstName", "lastName", "userName", "Password", true);
		final String expected = "UserModel{id=1, firstName='firstName', lastName='lastName', userName='userName', isActive=true}";

		assertEquals(expected, user2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		user = new UserModel(1L, "firstName", "lastName", "userName", "Password", true);
		final var user2 = new UserModel(2L, "firstName", "lastName", "userName", "Password", true);
		final var user3 = new UserModel(1L, "firstName2", "lastName", "userName", "Password", true);
		final var user4 = new UserModel(1L, "firstName", "lastName2", "userName", "Password", true);
		final var user5 = new UserModel(1L, "firstName", "lastName", "userName1", "Password", true);
		final var user6 = new UserModel(1L, "firstName", "lastName", "userName", "Password2", true);
		final var user7 = new UserModel(1L, "firstName", "lastName", "userName", "Password", false);

		assertEquals(user, user);
		assertEquals(user.hashCode(), user.hashCode());
		assertNotEquals(null, user);
		assertNotEquals(user, user2);
		assertNotEquals(user.hashCode(), user2.hashCode());
		assertNotEquals(user, user3);
		assertNotEquals(user, user4);
		assertNotEquals(user, user5);
		assertNotEquals(user, user6);
		assertNotEquals(user, user7);
	}
}
