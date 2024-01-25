package com.epam.dmgolub.gym.dto.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TraineeRequestDTOTest {

	private TraineeRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeRequestDTO();
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setFirstName("FirstName");
		assertEquals("FirstName", requestDTO.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setLastName("LastName");
		assertEquals("LastName", requestDTO.getLastName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setActive(true);
		assertTrue(requestDTO.isActive());
	}

	@Test
	void dateOfBirthGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		requestDTO.setDateOfBirth(date);
		assertEquals(date, requestDTO.getDateOfBirth());
	}

	@Test
	void addressGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setAddress("Address");
		assertEquals("Address", requestDTO.getAddress());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TraineeRequestDTO trainee =
			new TraineeRequestDTO("firstName", "lastName", "firstName.lastName", true, null, "address");
		final String expected = "TraineeRequestDTO{firstName='firstName', lastName='lastName', " +
			"userName='firstName.lastName', isActive=true, dateOfBirth=null, address='address'}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEquals() {
		final TraineeRequestDTO requestDTO2 =
			new TraineeRequestDTO("FirstName", "LastName", "FirstName.LastName", true, new Date(), "Address");
		final TraineeRequestDTO requestDTO3 =
			new TraineeRequestDTO("FirstName2", "LastName", "FirstName.LastName", true, new Date(), "Address");
		final TraineeRequestDTO requestDTO4 =
			new TraineeRequestDTO("FirstName", "LastName3", "FirstName.LastName", true, new Date(), "Address");
		final TraineeRequestDTO requestDTO5 =
			new TraineeRequestDTO("FirstName", "LastName","FirstName.LastName", true, new Date(), "Address2");
		final TraineeRequestDTO requestDTO6 =
			new TraineeRequestDTO("FirstName", "LastName", "FirstName.LastName2", true, new Date(), "Address");

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
		assertNotEquals(requestDTO2, requestDTO6);
	}

	@Test
	void testHashCode() {
		final TraineeRequestDTO requestDTO2 =
			new TraineeRequestDTO("FirstName", "LastName", "FirstName.LastName", true, new Date(), "Address");

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
