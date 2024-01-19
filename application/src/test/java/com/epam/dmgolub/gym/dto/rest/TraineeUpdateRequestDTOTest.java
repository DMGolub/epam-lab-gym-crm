package com.epam.dmgolub.gym.dto.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeUpdateRequestDTOTest {

	private TraineeUpdateRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeUpdateRequestDTO();
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
		requestDTO.setIsActive(true);
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
		final TraineeUpdateRequestDTO trainee =
			new TraineeUpdateRequestDTO( "firstName.lastName", "firstName", "lastName", null, "address", true);
		final String expected = "TraineeUpdateRequestDTO{userName='firstName.lastName', firstName='firstName', lastName='lastName', " +
			"dateOfBirth=null, address='address', isActive=true}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEquals() {
		final var requestDTO2 =
			new TraineeUpdateRequestDTO("FirstName.LastName", "FirstName", "LastName", new Date(), "Address", true);
		final var requestDTO3 =
			new TraineeUpdateRequestDTO("FirstName.LastName", "FirstName2", "LastName", new Date(), "Address", true);
		final var requestDTO4 =
			new TraineeUpdateRequestDTO("FirstName.LastName", "FirstName", "LastName3", new Date(), "Address", true);
		final var requestDTO5 =
			new TraineeUpdateRequestDTO("FirstName.LastName", "FirstName", "LastName", new Date(), "Address2", true);
		final var requestDTO6 =
			new TraineeUpdateRequestDTO("FirstName.LastName2", "FirstName", "LastName", new Date(), "Address", true);

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
		final var requestDTO2 =
			new TraineeUpdateRequestDTO("FirstName.LastName", "FirstName", "LastName", new Date(), "Address", true);

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
