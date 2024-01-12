package com.epam.dmgolub.gym.repository.searchcriteria;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TrainerTrainingsSearchCriteria implements Serializable {

	private String trainerUserName;
	private Date periodFrom;
	private Date periodTo;
	private String traineeUserName;

	public TrainerTrainingsSearchCriteria() {
		// Empty
	}

	public TrainerTrainingsSearchCriteria(
		final String trainerUserName,
		final Date periodFrom,
		final Date periodTo,
		final String traineeUserName
	) {
		this.trainerUserName = trainerUserName;
		this.periodFrom = periodFrom;
		this.periodTo = periodTo;
		this.traineeUserName = traineeUserName;
	}

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	public Date getPeriodFrom() {
		return periodFrom;
	}

	public void setPeriodFrom(final Date periodFrom) {
		this.periodFrom = periodFrom;
	}

	public Date getPeriodTo() {
		return periodTo;
	}

	public void setPeriodTo(final Date periodTo) {
		this.periodTo = periodTo;
	}

	public String getTraineeUserName() {
		return traineeUserName;
	}

	public void setTraineeUserName(final String traineeUserName) {
		this.traineeUserName = traineeUserName;
	}

	@Override
	public String toString() {
		return "TrainerTrainingsSearchCriteria{" +
			"trainerUserName='" + trainerUserName + '\'' +
			", periodFrom=" + periodFrom +
			", periodTo=" + periodTo +
			", traineeUserName='" + traineeUserName + '\'' +
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
		final TrainerTrainingsSearchCriteria that = (TrainerTrainingsSearchCriteria) o;
		if (!Objects.equals(trainerUserName, that.trainerUserName)) {
			return false;
		}
		if (!Objects.equals(periodFrom, that.periodFrom)) {
			return false;
		}
		if (!Objects.equals(periodTo, that.periodTo)) {
			return false;
		}
		return Objects.equals(traineeUserName, that.traineeUserName);
	}

	@Override
	public int hashCode() {
		int result = trainerUserName != null ? trainerUserName.hashCode() : 0;
		result = 31 * result + (periodFrom != null ? periodFrom.hashCode() : 0);
		result = 31 * result + (periodTo != null ? periodTo.hashCode() : 0);
		result = 31 * result + (traineeUserName != null ? traineeUserName.hashCode() : 0);
		return result;
	}
}

