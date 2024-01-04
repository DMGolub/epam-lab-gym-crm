package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TraineeResponseDTOTest {

	private TraineeResponseDTO responseDTO;

	@BeforeEach
	public void setUp() {
		responseDTO = new TraineeResponseDTO();
	}

	@Test
	void userIdGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setUserId(1L);
		assertEquals(1L, responseDTO.getUserId());
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setFirstName("FirstName");
		assertEquals("FirstName", responseDTO.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setLastName("LastName");
		assertEquals("LastName", responseDTO.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setUserName("UserName");
		assertEquals("UserName", responseDTO.getUserName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setActive(true);
		assertTrue(responseDTO.isActive());
	}

	@Test
	void idGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setId(2L);
		assertEquals(2L, responseDTO.getId());
	}

	@Test
	void dateOfBirthGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		Date date = new Date();
		responseDTO.setDateOfBirth(date);
		assertEquals(date, responseDTO.getDateOfBirth());
	}

	@Test
	void addressGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setAddress("Address");
		assertEquals("Address", responseDTO.getAddress());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TraineeResponseDTO trainee =
			new TraineeResponseDTO(1L, "firstName", "lastName", "userName",true, 2L, null, "address");
		final String expected = "TraineeResponseDTO{userId=1, firstName='firstName', lastName='lastName', " +
			"userName='userName', isActive=true, id=2, dateOfBirth=null, address='address'}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeResponseDTO responseDTO2 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, new Date(), "Address"
		);

		assertEquals(responseDTO2, responseDTO2);
		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}
}
