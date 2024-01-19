package com.epam.dmgolub.gym.dto.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			new TraineeResponseDTO(1L, "firstName", "lastName", "userName",true, 2L, null, "address", new ArrayList<>());
		final String expected = "TraineeResponseDTO{userId=1, firstName='firstName', lastName='lastName', " +
			"userName='userName', isActive=true, id=2, dateOfBirth=null, address='address', trainers=[]}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeResponseDTO.TrainerDTO trainer = new TraineeResponseDTO.TrainerDTO();
		final TraineeResponseDTO responseDTO2 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO3 =
			new TraineeResponseDTO(2L, "FirstName", "LastName", "UserName", true, 2L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO4 =
			new TraineeResponseDTO(1L, "FirstName2", "LastName", "UserName", true, 2L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO5 =
			new TraineeResponseDTO(1L, "FirstName", "LastName2", "UserName", true, 2L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO6 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName2", true, 2L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO7 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName", true, 3L, new Date(), "Address", new ArrayList<>());
		final TraineeResponseDTO responseDTO8 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, new Date(), "Address2", new ArrayList<>());
		final TraineeResponseDTO responseDTO9 =
			new TraineeResponseDTO(1L, "FirstName", "LastName", "UserName", true, 2L, new Date(), "Address", List.of(trainer));

		assertEquals(responseDTO2, responseDTO2);
		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
		assertNotEquals(responseDTO2, responseDTO9);
	}
}
