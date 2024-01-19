package com.epam.dmgolub.gym.dto.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerTrainingsSearchRequestDTOTest {

	private TrainerTrainingsSearchRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerTrainingsSearchRequestDTO();
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setUserName(userName);
		assertEquals(userName, requestDTO.getUserName());
	}

	@Test
	void periodFromGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		requestDTO.setPeriodFrom(date);
		assertEquals(date, requestDTO.getPeriodFrom());
	}

	@Test
	void periodToGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		requestDTO.setPeriodTo(date);
		assertEquals(date, requestDTO.getPeriodTo());
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setTraineeUserName(userName);
		assertEquals(userName, requestDTO.getTraineeUserName());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainerTrainingsSearchRequestDTO requestDTO2 =
			new TrainerTrainingsSearchRequestDTO("Trainer", null, null, "Trainee");
		final String expected = "TrainerTrainingsSearchRequestDTO{trainerUserName='Trainer', " +
			"periodFrom=null, periodTo=null, traineeUserName='Trainee'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainerTrainingsSearchRequestDTO requestDTO2 =
			new TrainerTrainingsSearchRequestDTO("Trainer", null, null, "Trainee");
		final TrainerTrainingsSearchRequestDTO requestDTO3 =
			new TrainerTrainingsSearchRequestDTO("Trainer", null, null, "Trainee3");

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO2, requestDTO3);
	}
}
