package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeResponseDTOTest {

	private TraineeResponseDTO responseDTO;
	private TraineeResponseDTO.TrainerDTO trainerDTO;

	@BeforeEach
	public void setUp() {
		responseDTO = new TraineeResponseDTO();
		trainerDTO = new TraineeResponseDTO.TrainerDTO();
	}

	@Test
	void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String firstName = "FirstName";
		responseDTO.setFirstName(firstName);
		assertEquals(firstName, responseDTO.getFirstName());
	}

	@Test
	void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String lastName = "LastName";
		responseDTO.setLastName(lastName);
		assertEquals(lastName, responseDTO.getLastName());
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		responseDTO.setUserName(userName);
		assertEquals(userName, responseDTO.getUserName());
	}

	@Test
	void dateOfBirth_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date dateOfBirth = new Date();
		responseDTO.setDateOfBirth(dateOfBirth);
		assertEquals(dateOfBirth, responseDTO.getDateOfBirth());
	}

	@Test
	void address_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String address = "Some address";
		responseDTO.setAddress(address);
		assertEquals(address, responseDTO.getAddress());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setActive(true);
		assertTrue(responseDTO.isActive());
	}

	@Test
	void trainersGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var trainers = List.of(new TraineeResponseDTO.TrainerDTO(), new TraineeResponseDTO.TrainerDTO());
		responseDTO.setTrainers(trainers);

		assertEquals(trainers, responseDTO.getTrainers());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final List<TraineeResponseDTO.TrainerDTO> trainers = new ArrayList<>();
		final var trainee =
			new TraineeResponseDTO("userName", "firstName", "lastName", null, "Address", true, trainers);
		final String expected = "TraineeResponseDTO{userName='userName', firstName='firstName', lastName='lastName', " +
			"dateOfBirth=null, address='Address', isActive=true, trainers=[]}";

		assertEquals(expected, trainee.toString());
	}

	@Test
	void testEquals() {
		final List<TraineeResponseDTO.TrainerDTO> trainers = new ArrayList<>();
		final Date dateOfBirth = new Date();
		final String address = "Some address";
		final var responseDTO2 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", dateOfBirth, address, true, null);
		final var responseDTO3 =
			new TraineeResponseDTO("UserName", "FirstName2", "LastName", dateOfBirth, address, true, null);
		final var responseDTO4 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName2", dateOfBirth, address, true, null);
		final var responseDTO5 =
			new TraineeResponseDTO("UserName2", "FirstName", "LastName", dateOfBirth, address, true, null);
		final var responseDTO6 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", null, address, true, null);
		final var responseDTO7 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", dateOfBirth, "Address", true, null);
		final var responseDTO8 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", dateOfBirth, address, false, null);
		final var responseDTO9 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", dateOfBirth, address, true, trainers);

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
		assertNotEquals(responseDTO2, responseDTO9);
	}

	@Test
	void testHashCode() {
		final List<TraineeResponseDTO.TrainerDTO> trainers = new ArrayList<>();
		final Date dateOfBirth = new Date();
		final String address = "Some address";
		final var responseDTO2 =
			new TraineeResponseDTO("UserName", "FirstName", "LastName", dateOfBirth, address, true, trainers);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}

	@Nested
	class TrainerDTOTest {

		@Test
		void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String firstName = "FirstName";
			trainerDTO.setFirstName(firstName);
			assertEquals("FirstName", trainerDTO.getFirstName());
		}

		@Test
		void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String lastName = "LastName";
			trainerDTO.setLastName(lastName);
			assertEquals(lastName, trainerDTO.getLastName());
		}

		@Test
		void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String userName = "UserName";
			trainerDTO.setUserName(userName);
			assertEquals(userName, trainerDTO.getUserName());
		}

		@Test
		void specialization_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String typeReference = "/api/v1/training-types/1";
			trainerDTO.setSpecialization(typeReference);
			assertEquals(typeReference, trainerDTO.getSpecialization());
		}

		@Test
		void toString_shouldReturnExpectedValue_whenInvoked() {
			final String typeReference = "/api/v1/training-types/1";
			trainerDTO = new TraineeResponseDTO.TrainerDTO("FirstName.LastName", "FirstName", "LastName", typeReference);
			final String expected = "TrainerDTO{userName='FirstName.LastName', firstName='FirstName', " +
				"lastName='LastName', specialization=" + typeReference + "}";

			assertEquals(expected, trainerDTO.toString());
		}

		@Test
		void testEqualsAndHashCode() {
			final String typeReference = "/api/v1/training-types/1";
			trainerDTO =
				new TraineeResponseDTO.TrainerDTO("FirstName.LastName", "FirstName", "LastName", typeReference);
			final var trainerDTO2 =
				new TraineeResponseDTO.TrainerDTO("FirstName.LastName2", "FirstName", "LastName", typeReference);
			final var trainerDTO3 =
				new TraineeResponseDTO.TrainerDTO("FirstName.LastName", "FirstName2", "LastName", typeReference);
			final var trainerDTO4 =
				new TraineeResponseDTO.TrainerDTO("FirstName.LastName", "FirstName", "LastName3", typeReference);
			final var trainerDTO5 =
				new TraineeResponseDTO.TrainerDTO("FirstName.LastName", "FirstName", "LastName", "/api/v1/training-types/2");

			assertEquals(trainerDTO2, trainerDTO2);
			assertEquals(trainerDTO2.hashCode(), trainerDTO2.hashCode());
			assertNotEquals(trainerDTO, trainerDTO2);
			assertNotEquals(trainerDTO.hashCode(), trainerDTO2.hashCode());
			assertNotEquals(null, trainerDTO2);
			assertNotEquals(trainerDTO, trainerDTO3);
			assertNotEquals(trainerDTO.hashCode(), trainerDTO3.hashCode());
			assertNotEquals(trainerDTO, trainerDTO4);
			assertNotEquals(trainerDTO.hashCode(), trainerDTO4.hashCode());
			assertNotEquals(trainerDTO, trainerDTO5);
			assertNotEquals(trainerDTO.hashCode(), trainerDTO5.hashCode());
		}
	}
}
