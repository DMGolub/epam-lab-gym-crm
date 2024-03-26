package com.epam.dmgolub.gym.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "workload")
@CompoundIndex(def = "{'trainerFirstName': 1, 'trainerLastName': 1}")
public class TrainerWorkload {

	@Id
	@Indexed
	private String trainerUserName;
	private String trainerFirstName;
	private String trainerLastName;
	private boolean trainerStatus;
	private List<Year> years;

	public TrainerWorkload() {
		years = new ArrayList<>();
	}

	public TrainerWorkload(
		final String trainerUserName,
		final String trainerFirstName,
		final String trainerLastName,
		final boolean trainerStatus,
		final List<Year> years
	) {
		this.trainerUserName = trainerUserName;
		this.trainerFirstName = trainerFirstName;
		this.trainerLastName = trainerLastName;
		this.trainerStatus = trainerStatus;
		this.years = years;
	}

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	public String getTrainerFirstName() {
		return trainerFirstName;
	}

	public void setTrainerFirstName(final String trainerFirstName) {
		this.trainerFirstName = trainerFirstName;
	}

	public String getTrainerLastName() {
		return trainerLastName;
	}

	public void setTrainerLastName(final String trainerLastName) {
		this.trainerLastName = trainerLastName;
	}

	public boolean getTrainerStatus() {
		return trainerStatus;
	}

	public void setTrainerStatus(final boolean trainerStatus) {
		this.trainerStatus = trainerStatus;
	}

	public List<Year> getYears() {
		return years;
	}

	public void setYears(final List<Year> years) {
		this.years = years;
	}

	@Override
	public String toString() {
		return "TrainerWorkload{" +
			"trainerUserName='" + trainerUserName + '\'' +
			", trainerFirstName='" + trainerFirstName + '\'' +
			", trainerLastName='" + trainerLastName + '\'' +
			", trainerStatus=" + trainerStatus +
			", years=" + years +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainerWorkload that = (TrainerWorkload) o;
		if (trainerStatus != that.trainerStatus) {
			return false;
		}
		if (!Objects.equals(trainerUserName, that.trainerUserName)) {
			return false;
		}
		if (!Objects.equals(trainerFirstName, that.trainerFirstName)) {
			return false;
		}
		if (!Objects.equals(trainerLastName, that.trainerLastName)) {
			return false;
		}
		return Objects.equals(years, that.years);
	}

	@Override
	public int hashCode() {
		int result = trainerUserName != null ? trainerUserName.hashCode() : 0;
		result = 31 * result + (trainerFirstName != null ? trainerFirstName.hashCode() : 0);
		result = 31 * result + (trainerLastName != null ? trainerLastName.hashCode() : 0);
		result = 31 * result + (trainerStatus ? 1 : 0);
		result = 31 * result + (years != null ? years.hashCode() : 0);
		return result;
	}

	public static class Year {

		private int value;
		private List<Month> months;

		public Year() {
			months = new ArrayList<>();
		}

		public Year(final int value, final List<Month> months) {
			this.value = value;
			this.months = months;
		}

		public int getValue() {
			return value;
		}

		public void setValue(final int value) {
			this.value = value;
		}

		public List<Month> getMonths() {
			return months;
		}

		public void setMonths(final List<Month> months) {
			this.months = months;
		}

		@Override
		public String toString() {
			return "Year{" +
				"value=" + value +
				", months=" + months +
				'}';
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			final Year year = (Year) o;
			if (value != year.value) {
				return false;
			}
			return Objects.equals(months, year.months);
		}

		@Override
		public int hashCode() {
			int result = value;
			result = 31 * result + (months != null ? months.hashCode() : 0);
			return result;
		}
	}

	public static class Month {

		private int value;
		private int trainingSummaryDuration;

		public Month() {
			// Empty
		}

		public Month(final int value, final int trainingSummaryDuration) {
			this.value = value;
			this.trainingSummaryDuration = trainingSummaryDuration;
		}

		public int getValue() {
			return value;
		}

		public void setValue(final int value) {
			this.value = value;
		}

		public int getTrainingSummaryDuration() {
			return trainingSummaryDuration;
		}

		public void setTrainingSummaryDuration(final int trainingSummaryDuration) {
			this.trainingSummaryDuration = trainingSummaryDuration;
		}

		@Override
		public String toString() {
			return "Month{" +
				"value=" + value +
				", trainingSummaryDuration=" + trainingSummaryDuration +
				'}';
		}

		@Override
		public boolean equals(final Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}
			final Month month = (Month) o;
			if (value != month.value) {
				return false;
			}
			return trainingSummaryDuration == month.trainingSummaryDuration;
		}

		@Override
		public int hashCode() {
			int result = value;
			result = 31 * result + trainingSummaryDuration;
			return result;
		}
	}
}
