package com.epam.dmgolub.gym.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerWorkloadTest {

	private TrainerWorkload trainerWorkload;

	@BeforeEach
	public void setUp() {
		trainerWorkload = new TrainerWorkload();
	}

	@Test
	void trainerUserName_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var userName = "User.Name";
		trainerWorkload.setTrainerUserName(userName);
		assertEquals(userName, trainerWorkload.getTrainerUserName());
	}

	@Test
	void trainerFirstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var firstName = "FirstName";
		trainerWorkload.setTrainerFirstName(firstName);
		assertEquals(firstName, trainerWorkload.getTrainerFirstName());
	}

	@Test
	void trainerLastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var lastName = "LastName";
		trainerWorkload.setTrainerLastName(lastName);
		assertEquals(lastName, trainerWorkload.getTrainerLastName());
	}

	@Test
	void trainerStatusGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		trainerWorkload.setTrainerStatus(true);
		assertTrue(trainerWorkload.getTrainerStatus());
	}

	@Test
	void yearsStatusGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final var years = List.of(
			new TrainerWorkload.Year(2023, new ArrayList<>()),
			new TrainerWorkload.Year(2024, new ArrayList<>())
		);
		trainerWorkload.setYears(years);
		assertEquals(years, trainerWorkload.getYears());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		trainerWorkload = new TrainerWorkload(
			"User.Name",
			"FirstName",
			"LastName",
			true,
			new ArrayList<>()
		);
		final var expected = "TrainerWorkload{trainerUserName='User.Name', trainerFirstName='FirstName', " +
			"trainerLastName='LastName', trainerStatus=true, years=[]}";
		assertEquals(expected, trainerWorkload.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final var years = List.of(
			new TrainerWorkload.Year(2023, new ArrayList<>()),
			new TrainerWorkload.Year(2024, new ArrayList<>())
		);
		trainerWorkload =
			new TrainerWorkload("User.Name", "FirstName", "LastName", true, years);
		final var trainerWorkload2 =
			new TrainerWorkload("User.Name", "FirstName", "LastName", true, years);
		final var trainerWorkload3 =
			new TrainerWorkload("User.Name2", "FirstName", "LastName", true, years);
		final var trainerWorkload4 =
			new TrainerWorkload("User.Name", "FirstName2", "LastName", true, years);
		final var trainerWorkload5 =
			new TrainerWorkload("User.Name", "FirstName", "LastName2", true, years);
		final var trainerWorkload6 =
			new TrainerWorkload("User.Name", "FirstName", "LastName", false, years);
		final var trainerWorkload7 =
			new TrainerWorkload("User.Name", "FirstName", "LastName", true, new ArrayList<>());

		assertEquals(trainerWorkload, trainerWorkload2);
		assertEquals(trainerWorkload.hashCode(), trainerWorkload2.hashCode());
		assertNotEquals(trainerWorkload, trainerWorkload3);
		assertNotEquals(trainerWorkload.hashCode(), trainerWorkload3.hashCode());
		assertNotEquals(trainerWorkload, trainerWorkload4);
		assertNotEquals(trainerWorkload.hashCode(), trainerWorkload4.hashCode());
		assertNotEquals(trainerWorkload, trainerWorkload5);
		assertNotEquals(trainerWorkload.hashCode(), trainerWorkload5.hashCode());
		assertNotEquals(trainerWorkload, trainerWorkload6);
		assertNotEquals(trainerWorkload.hashCode(), trainerWorkload6.hashCode());
		assertNotEquals(trainerWorkload, trainerWorkload7);
		assertNotEquals(trainerWorkload.hashCode(), trainerWorkload7.hashCode());
	}

	@Nested
	class TestYear {

		private TrainerWorkload.Year year;

		@BeforeEach
		public void setUp() {
			this.year = new TrainerWorkload.Year();
		}

		@Test
		void valueGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final int value = 2024;
			year.setValue(value);
			assertEquals(value, year.getValue());
		}

		@Test
		void monthsGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final var months = List.of(
				new TrainerWorkload.Month(1, 0),
				new TrainerWorkload.Month(2, 0)
			);
			year.setMonths(months);
			assertEquals(months, year.getMonths());
		}

		@Test
		void toString_shouldReturnExpectedValue_whenInvoked() {
			final var value = 2024;
			final var months = List.of(
				new TrainerWorkload.Month(1, 45),
				new TrainerWorkload.Month(2, 90)
			);
			year = new TrainerWorkload.Year(value, months);
			final var expected = "Year{value=" + value + ", months=" + months + "}";
			assertEquals(expected, year.toString());
		}

		@Test
		void testEqualsAndHashCode() {
			final var months = List.of(
				new TrainerWorkload.Month(1, 45),
				new TrainerWorkload.Month(2, 90)
			);
			year = new TrainerWorkload.Year(2024, months);
			final var year2 = new TrainerWorkload.Year(2024, months);
			final var year3 = new TrainerWorkload.Year(2025, months);
			final var year4 = new TrainerWorkload.Year(2024, new ArrayList<>());

			assertEquals(year, year2);
			assertEquals(year.hashCode(), year2.hashCode());
			assertNotEquals(year, year3);
			assertNotEquals(year.hashCode(), year3.hashCode());
			assertNotEquals(year, year4);
			assertNotEquals(year.hashCode(), year4.hashCode());
		}
	}

	@Nested
	class TestMonth {

		private TrainerWorkload.Month month;

		@BeforeEach
		public void setUp() {
			this.month = new TrainerWorkload.Month();
		}

		@Test
		void valueGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final int value = 2024;
			month.setValue(value);
			assertEquals(value, month.getValue());
		}

		@Test
		void trainingSummaryDurationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
			final int duration = 90;
			month.setTrainingSummaryDuration(duration);
			assertEquals(duration, month.getTrainingSummaryDuration());
		}

		@Test
		void toString_shouldReturnExpectedValue_whenInvoked() {
			month = new TrainerWorkload.Month(1, 90);
			final var expected = "Month{value=1, trainingSummaryDuration=90}";
			assertEquals(expected, month.toString());
		}

		@Test
		void testEqualsAndHashCode() {
			month = new TrainerWorkload.Month(1, 90);
			final var month2 = new TrainerWorkload.Month(1, 90);
			final var month3 = new TrainerWorkload.Month(2, 90);
			final var month4 = new TrainerWorkload.Month(1, 45);

			assertEquals(month, month2);
			assertEquals(month.hashCode(), month2.hashCode());
			assertNotEquals(month, month3);
			assertNotEquals(month.hashCode(), month3.hashCode());
			assertNotEquals(month, month4);
			assertNotEquals(month.hashCode(), month4.hashCode());
		}
	}
}
