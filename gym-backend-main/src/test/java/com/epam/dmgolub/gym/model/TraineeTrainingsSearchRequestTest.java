package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeTrainingsSearchRequestTest {

	private TraineeTrainingsSearchRequest requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeTrainingsSearchRequest();
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setTraineeUserName(userName);
		assertEquals(userName, requestDTO.getTraineeUserName());
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
	void trainerUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setTrainerUserName(userName);
		assertEquals(userName, requestDTO.getTrainerUserName());
	}

	@Test
	void trainingTypeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final TrainingTypeModel type = new TrainingTypeModel();
		requestDTO.setType(type);
		assertEquals(type, requestDTO.getType());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final var requestDTO2 =
			new TraineeTrainingsSearchRequest("Trainee", null, null, "Trainer", null);
		final String expected = "TraineeTrainingsSearchRequest{traineeUserName='Trainee', " +
			"periodFrom=null, periodTo=null, trainerUserName='Trainer', type=null}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final var requestDTO2 =
			new TraineeTrainingsSearchRequest("Trainee", null, null, "Trainer", null);
		final var requestDTO3 =
			new TraineeTrainingsSearchRequest("Trainee", null, null, "Trainer3", null);
		final var requestDTO4 =
			new TraineeTrainingsSearchRequest("Trainee", null, null, "Trainer", new TrainingTypeModel());

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
	}
}
