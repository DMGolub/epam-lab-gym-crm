package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerResponseDTOTest {

	private TrainerResponseDTO responseDTO;
	private TrainerResponseDTO.TraineeDTO traineeDTO;

	@BeforeEach
	public void setUp() {
		responseDTO = new TrainerResponseDTO();
		traineeDTO = new TrainerResponseDTO.TraineeDTO();
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
	void specializationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String typeReference = "/api/v1/training-types/1";
		responseDTO.setSpecialization(typeReference);
		assertEquals(typeReference, responseDTO.getSpecialization());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		responseDTO.setActive(true);
		assertTrue(responseDTO.isActive());
	}

	@Test
	void traineesGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var trainees = List.of(new TrainerResponseDTO.TraineeDTO(), new TrainerResponseDTO.TraineeDTO());
		responseDTO.setTrainees(trainees);

		assertEquals(trainees, responseDTO.getTrainees());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final String typeReference = "/api/v1/training-types/1";
		final List<TrainerResponseDTO.TraineeDTO> trainees = new ArrayList<>();
		final var trainer =
			new TrainerResponseDTO("userName", "firstName", "lastName", typeReference, true, trainees);
		final String expected = "TrainerResponseDTO{userName='userName', firstName='firstName', lastName='lastName', " +
			"specialization=" + typeReference + ", isActive=true, trainees=[]}";

		assertEquals(expected, trainer.toString());
	}

	@Test
	void testEquals() {
		final String typeReference = "/api/v1/training-types/1";
		final List<TrainerResponseDTO.TraineeDTO> trainees = new ArrayList<>();
		final var responseDTO2 =
			new TrainerResponseDTO("UserName", "FirstName", "LastName", typeReference, true, null);
		final var responseDTO3 =
			new TrainerResponseDTO("UserName", "FirstName2", "LastName", typeReference, true, null);
		final var responseDTO4 =
			new TrainerResponseDTO("UserName", "FirstName", "LastName2", typeReference, true, null);
		final var responseDTO5 =
			new TrainerResponseDTO("UserName2", "FirstName", "LastName", typeReference, true, null);
		final var responseDTO6 =
			new TrainerResponseDTO("UserName", "FirstName", "LastName", "/api/v1/training-types/2", true, null);
		final var responseDTO7 =
			new TrainerResponseDTO("UserName2", "FirstName", "LastName", typeReference, false, null);
		final var responseDTO8 =
			new TrainerResponseDTO("UserName2", "FirstName", "LastName", typeReference, true, trainees);

		assertEquals(responseDTO2, responseDTO2);
		assertNotEquals(responseDTO, responseDTO2);
		assertNotEquals(null, responseDTO2);
		assertNotEquals(responseDTO2, responseDTO3);
		assertNotEquals(responseDTO2, responseDTO4);
		assertNotEquals(responseDTO2, responseDTO5);
		assertNotEquals(responseDTO2, responseDTO6);
		assertNotEquals(responseDTO2, responseDTO7);
		assertNotEquals(responseDTO2, responseDTO8);
	}

	@Test
	void testHashCode() {
		final String typeReference = "/api/v1/training-types/1";
		final TrainerResponseDTO responseDTO2 =
			new TrainerResponseDTO("UserName", "FirstName", "LastName", typeReference, true, null);

		assertEquals(responseDTO2.hashCode(), responseDTO2.hashCode());
		assertNotEquals(responseDTO.hashCode(), responseDTO2.hashCode());
	}

	@Nested
	class TraineeDTOTest {

		@Test
		void firstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String firstName = "FirstName";
			traineeDTO.setFirstName(firstName);
			assertEquals("FirstName", traineeDTO.getFirstName());
		}

		@Test
		void lastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String lastName = "LastName";
			traineeDTO.setLastName(lastName);
			assertEquals(lastName, traineeDTO.getLastName());
		}

		@Test
		void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final String userName = "UserName";
			traineeDTO.setUserName(userName);
			assertEquals(userName, traineeDTO.getUserName());
		}

		@Test
		void toString_shouldReturnExpectedValue_whenInvoked() {
			traineeDTO = new TrainerResponseDTO.TraineeDTO("FirstName.LastName", "FirstName", "LastName");
			final String expected = "TraineeDTO{userName='FirstName.LastName', firstName='FirstName', lastName='LastName'}";

			assertEquals(expected, traineeDTO.toString());
		}

		@Test
		void testEqualsAndHashCode() {
			traineeDTO =
				new TrainerResponseDTO.TraineeDTO("FirstName.LastName", "FirstName", "LastName");
			final var traineeDTO2 =
				new TrainerResponseDTO.TraineeDTO("FirstName.LastName2", "FirstName", "LastName");
			final var traineeDTO3 =
				new TrainerResponseDTO.TraineeDTO("FirstName.LastName", "FirstName2", "LastName");
			final var traineeDTO4 =
				new TrainerResponseDTO.TraineeDTO("FirstName.LastName", "FirstName", "LastName3");

			assertEquals(traineeDTO2, traineeDTO2);
			assertEquals(traineeDTO2.hashCode(), traineeDTO2.hashCode());
			assertNotEquals(traineeDTO, traineeDTO2);
			assertNotEquals(traineeDTO.hashCode(), traineeDTO2.hashCode());
			assertNotEquals(null, traineeDTO2);
			assertNotEquals(traineeDTO, traineeDTO3);
			assertNotEquals(traineeDTO.hashCode(), traineeDTO3.hashCode());
			assertNotEquals(traineeDTO, traineeDTO4);
			assertNotEquals(traineeDTO.hashCode(), traineeDTO4.hashCode());
		}
	}
}
