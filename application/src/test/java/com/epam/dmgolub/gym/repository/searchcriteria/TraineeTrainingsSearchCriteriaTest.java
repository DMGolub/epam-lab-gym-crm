package com.epam.dmgolub.gym.repository.searchcriteria;

import com.epam.dmgolub.gym.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TraineeTrainingsSearchCriteriaTest {
	
	private TraineeTrainingsSearchCriteria searchCriteria;
	
	@BeforeEach
	public void setUp() {
		searchCriteria = new TraineeTrainingsSearchCriteria();
	}

	@Test
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		searchCriteria.setTraineeUserName(userName);
		assertEquals(userName, searchCriteria.getTraineeUserName());
	}

	@Test
	void periodFromGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		searchCriteria.setPeriodFrom(date);
		assertEquals(date, searchCriteria.getPeriodFrom());
	}

	@Test
	void periodToGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		searchCriteria.setPeriodTo(date);
		assertEquals(date, searchCriteria.getPeriodTo());
	}

	@Test
	void trainerUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		searchCriteria.setTrainerUserName(userName);
		assertEquals(userName, searchCriteria.getTrainerUserName());
	}

	@Test
	void trainingTypeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final TrainingType type = new TrainingType();
		searchCriteria.setType(type);
		assertEquals(type, searchCriteria.getType());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TraineeTrainingsSearchCriteria searchCriteria2 =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, "Trainer", null);
		final String expected = "TraineeTrainingsSearchCriteria{traineeUserName='Trainee', " +
			"periodFrom=null, periodTo=null, trainerUserName='Trainer', type=null}";

		assertEquals(expected, searchCriteria2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TraineeTrainingsSearchCriteria searchCriteria2 =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, "Trainer", null);
		final TraineeTrainingsSearchCriteria searchCriteria3 =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, "Trainer3", null);
		final TraineeTrainingsSearchCriteria searchCriteria4 =
			new TraineeTrainingsSearchCriteria("Trainee", null, null, "Trainer", new TrainingType());

		assertEquals(searchCriteria2, searchCriteria2);
		assertEquals(searchCriteria2.hashCode(), searchCriteria2.hashCode());
		assertNotEquals(searchCriteria, searchCriteria2);
		assertNotEquals(searchCriteria.hashCode(), searchCriteria2.hashCode());
		assertNotEquals(searchCriteria2, searchCriteria3);
		assertNotEquals(searchCriteria2, searchCriteria4);
	}
}
