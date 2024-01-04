package com.epam.dmgolub.gym.dto;

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
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setUserId(1L);
		assertEquals(1L, requestDTO.getUserId());
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
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setId(2L);
		assertEquals(2L, requestDTO.getId());
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
			new TraineeRequestDTO(1L, "firstName", "lastName", true, 2L, null, "address");
		final String expected = "TraineeRequestDTO{userId=1, firstName='firstName', lastName='lastName', isActive=true, " +
			"id=2, dateOfBirth=null, address='address'}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeRequestDTO requestDTO2 =
			new TraineeRequestDTO(1L, "FirstName", "LastName", true, 2L, new Date(), "Address");

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
	}
}
