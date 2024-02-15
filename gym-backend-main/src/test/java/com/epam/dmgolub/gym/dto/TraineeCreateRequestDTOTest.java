package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeCreateRequestDTOTest {

	private TraineeCreateRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeCreateRequestDTO();
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
		final TraineeCreateRequestDTO trainee =
			new TraineeCreateRequestDTO("firstName", "lastName", null, "address");
		final String expected =
			"TraineeCreateRequestDTO{firstName='firstName', lastName='lastName', dateOfBirth=null, address='address'}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEquals() {
		final var requestDTO2 =
			new TraineeCreateRequestDTO("FirstName", "LastName", new Date(), "Address");
		final var requestDTO3 =
			new TraineeCreateRequestDTO("FirstName2", "LastName", new Date(), "Address");
		final var requestDTO4 =
			new TraineeCreateRequestDTO("FirstName", "LastName3", new Date(), "Address");
		final var requestDTO5 =
			new TraineeCreateRequestDTO("FirstName", "LastName", new Date(), "Address2");

		assertEquals(requestDTO2, requestDTO2);
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2, requestDTO5);
	}

	@Test
	void testHashCode() {
		final var requestDTO2 = new TraineeCreateRequestDTO("FirstName", "LastName", new Date(), "Address");

		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
