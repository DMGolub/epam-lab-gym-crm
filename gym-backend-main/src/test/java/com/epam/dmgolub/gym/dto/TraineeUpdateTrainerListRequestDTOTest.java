package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeUpdateTrainerListRequestDTOTest {

	private TraineeUpdateTrainerListRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeUpdateTrainerListRequestDTO();
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		requestDTO.setTraineeUserName(userName);
		assertEquals(userName, requestDTO.getTraineeUserName());
	}

	@Test
	void trainerUserNamesGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final List<String> trainerUserNames = List.of("User.Name", "User.Name2");
		requestDTO.setTrainerUserNames(trainerUserNames);
		assertEquals(trainerUserNames, requestDTO.getTrainerUserNames());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final String traineeUserName = "User.Name";
		final List<String> trainerUserNames = List.of("User.Name2", "User.Name3");
		requestDTO = new TraineeUpdateTrainerListRequestDTO(traineeUserName, trainerUserNames);
		final String expected = "TraineeUpdateTrainerListRequestDTO{" +
			"traineeUserName='User.Name', trainerUserNames=[User.Name2, User.Name3]}";

		assertEquals(expected, requestDTO.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final String traineeUserName = "User.Name";
		final List<String> trainerUserNames = List.of("User.Name2", "User.Name3");
		final var requestDTO2 = new TraineeUpdateTrainerListRequestDTO(traineeUserName, trainerUserNames);
		final var requestDTO3 = new TraineeUpdateTrainerListRequestDTO("Other.Name", trainerUserNames);
		final var requestDTO4 = new TraineeUpdateTrainerListRequestDTO(traineeUserName, new ArrayList<>());

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2.hashCode(), requestDTO3.hashCode());
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2.hashCode(), requestDTO4.hashCode());
	}
}
