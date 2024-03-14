package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TraineeResponseDTOTest {

	private TraineeResponseDTO responseDTO;

	@BeforeEach
	public void setUp() {
		responseDTO = new TraineeResponseDTO();
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
			new TraineeResponseDTO("firstName", "lastName", "userName",true, null, "address", new ArrayList<>());
		final String expected = "TraineeResponseDTO{firstName='firstName', lastName='lastName', " +
			"userName='userName', isActive=true, dateOfBirth=null, address='address', trainers=[]}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeResponseDTO.TrainerDTO trainer = new TraineeResponseDTO.TrainerDTO();
		final TraineeResponseDTO responseDTO2 =
			new TraineeResponseDTO("FirstName", "LastName", "UserName", true, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO3 =
			new TraineeResponseDTO("FirstName2", "LastName", "UserName", true, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO4 =
			new TraineeResponseDTO("FirstName", "LastName2", "UserName", true, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO5 =
			new TraineeResponseDTO("FirstName", "LastName", "UserName2", true, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO6 =
			new TraineeResponseDTO("FirstName", "LastName", "UserName", true, new Date(), "Address2", new ArrayList<>());
		final TraineeResponseDTO responseDTO7 =
			new TraineeResponseDTO("FirstName", "LastName", "UserName", true, new Date(), "Address", List.of(trainer));

		assertEquals(responseDTO2, responseDTO2);
		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
	}
}
