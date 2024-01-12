package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TrainerTrainingsSearchRequestTest {

	private TrainerTrainingsSearchRequest requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerTrainingsSearchRequest();
	}

	@Test
	void trainerUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setTrainerUserName(userName);
		assertEquals(userName, requestDTO.getTrainerUserName());
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
		final var requestDTO2 = new TrainerTrainingsSearchRequest("Trainer", null, null, "Trainee");
		final String expected = "TrainerTrainingsSearchRequest{trainerUserName='Trainer', " +
			"periodFrom=null, periodTo=null, traineeUserName='Trainee'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final var requestDTO2 = new TrainerTrainingsSearchRequest("Trainer", null, null, "Trainee");
		final var requestDTO3 = new TrainerTrainingsSearchRequest("Trainer", null, null, "Trainee3");

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO2, requestDTO3);
	}
}
