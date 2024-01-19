package com.epam.dmgolub.gym.dto.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeTrainingsSearchRequestDTOTest {

	private TraineeTrainingsSearchRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TraineeTrainingsSearchRequestDTO();
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
		final TrainingTypeDTO type = new TrainingTypeDTO();
		requestDTO.setType(type);
		assertEquals(type, requestDTO.getType());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TraineeTrainingsSearchRequestDTO requestDTO2 =
			new TraineeTrainingsSearchRequestDTO("Trainee", null, null, "Trainer", null);
		final String expected = "TraineeTrainingsSearchRequestDTO{traineeUserName='Trainee', " +
			"periodFrom=null, periodTo=null, trainerUserName='Trainer', type=null}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeTrainingsSearchRequestDTO requestDTO2 =
			new TraineeTrainingsSearchRequestDTO("Trainee", null, null, "Trainer", null);
		final TraineeTrainingsSearchRequestDTO requestDTO3 =
			new TraineeTrainingsSearchRequestDTO("Trainee", null, null, "Trainer3", null);
		final TraineeTrainingsSearchRequestDTO requestDTO4 =
			new TraineeTrainingsSearchRequestDTO("Trainee", null, null, "Trainer", new TrainingTypeDTO());

		assertEquals(requestDTO2, requestDTO2);
		assertEquals(requestDTO2.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
	}
}
