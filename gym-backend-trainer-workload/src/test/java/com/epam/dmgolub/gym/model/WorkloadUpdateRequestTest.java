package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkloadUpdateRequestTest {
	
	private WorkloadUpdateRequest request;

	@BeforeEach
	public void setUp() {
		request = new WorkloadUpdateRequest();
	}

	@Test
	void trainerUserName_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		request.setTrainerUserName(userName);
		assertEquals(userName, request.getTrainerUserName());
	}

	@Test
	void trainerFirstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String firstName = "FirstName";
		request.setTrainerFirstName(firstName);
		assertEquals(firstName, request.getTrainerFirstName());
	}

	@Test
	void trainerLastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String lastName = "LastName";
		request.setTrainerLastName(lastName);
		assertEquals(lastName, request.getTrainerLastName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		request.setActive(true);
		assertTrue(request.getActive());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		request.setDate(date);
		assertEquals(date, request.getDate());
	}

	@Test
	void durationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final int duration = 90;
		request.setDuration(duration);
		assertEquals(duration, request.getDuration());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		request = new WorkloadUpdateRequest(
			"User.Name",
			"FirstName",
			"LastName",
			true,
			null,
			60
		);
		final String expected = "TrainingRequestDTO{trainerUserName='User.Name', trainerFirstName='FirstName', " +
			"trainerLastName='LastName', isActive=true, date=null, duration=60}";
		assertEquals(expected, request.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		request =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName", true, null, 60);
		final var request2 =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName", true, null, 60);
		final var request3 =
			new WorkloadUpdateRequest("User.Name2", "FirstName", "LastName", true, null, 60);
		final var request4 =
			new WorkloadUpdateRequest("User.Name", "FirstName2", "LastName", true, null, 60);
		final var request5 =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName2", true, null, 60);
		final var request6 =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName", false, null, 60);
		final var request7 =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName", true, new Date(), 60);
		final var request8 =
			new WorkloadUpdateRequest("User.Name", "FirstName", "LastName", true, null, 90);

		assertEquals(request, request2);
		assertEquals(request.hashCode(), request2.hashCode());
		assertNotEquals(request2, request3);
		assertNotEquals(request2.hashCode(), request3.hashCode());
		assertNotEquals(request2, request4);
		assertNotEquals(request2.hashCode(), request4.hashCode());
		assertNotEquals(request2, request5);
		assertNotEquals(request2.hashCode(), request5.hashCode());
		assertNotEquals(request2, request6);
		assertNotEquals(request2.hashCode(), request6.hashCode());
		assertNotEquals(request2, request7);
		assertNotEquals(request2.hashCode(), request7.hashCode());
		assertNotEquals(request2, request8);
		assertNotEquals(request2.hashCode(), request8.hashCode());
	}
}
