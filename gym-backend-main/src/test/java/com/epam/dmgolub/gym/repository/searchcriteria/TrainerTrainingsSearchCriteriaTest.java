package com.epam.dmgolub.gym.repository.searchcriteria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTrainingsSearchCriteriaTest {

	private TrainerTrainingsSearchCriteria searchCriteria;

	@BeforeEach
	public void setUp() {
		searchCriteria = new TrainerTrainingsSearchCriteria();
	}

	@Test
	void trainerUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		searchCriteria.setTrainerUserName(userName);
		assertEquals(userName, searchCriteria.getTrainerUserName());
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
	void traineeUserNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		searchCriteria.setTraineeUserName(userName);
		assertEquals(userName, searchCriteria.getTraineeUserName());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final TrainerTrainingsSearchCriteria searchCriteria2 =
			new TrainerTrainingsSearchCriteria("Trainer", null, null, "Trainee");
		final String expected = "TrainerTrainingsSearchCriteria{trainerUserName='Trainer', " +
			"periodFrom=null, periodTo=null, traineeUserName='Trainee'}";

		assertEquals(expected, searchCriteria2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final TrainerTrainingsSearchCriteria searchCriteria2 =
			new TrainerTrainingsSearchCriteria("Trainer", null, null, "Trainee");
		final TrainerTrainingsSearchCriteria searchCriteria3 =
			new TrainerTrainingsSearchCriteria("Trainer", null, null, "Trainee3");

		assertEquals(searchCriteria2, searchCriteria2);
		assertEquals(searchCriteria2.hashCode(), searchCriteria2.hashCode());
		assertNotEquals(searchCriteria, searchCriteria2);
		assertNotEquals(searchCriteria.hashCode(), searchCriteria2.hashCode());
		assertNotEquals(searchCriteria2, searchCriteria3);
	}
}
