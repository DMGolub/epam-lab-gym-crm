package com.epam.dmgolub.gym.model;

import java.util.Date;
import java.util.Objects;

public class TraineeTrainingsSearchRequest {

	private String traineeUserName;
	private Date periodFrom;
	private Date periodTo;
	private String trainerUserName;
	private TrainingTypeModel type;

	public TraineeTrainingsSearchRequest() {
		// Empty
	}

	public TraineeTrainingsSearchRequest(
		final String traineeUserName,
		final Date periodFrom,
		final Date periodTo,
		final String trainerUserName,
		final TrainingTypeModel type
	) {
		this.traineeUserName = traineeUserName;
		this.periodFrom = periodFrom;
		this.periodTo = periodTo;
		this.trainerUserName = trainerUserName;
		this.type = type;
	}

	public String getTraineeUserName() {
		return traineeUserName;
	}

	public void setTraineeUserName(final String traineeUserName) {
		this.traineeUserName = traineeUserName;
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

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	public TrainingTypeModel getType() {
		return type;
	}

	public void setType(final TrainingTypeModel type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TraineeTrainingsSearchRequest{" +
			"traineeUserName='" + traineeUserName + '\'' +
			", periodFrom=" + periodFrom +
			", periodTo=" + periodTo +
			", trainerUserName='" + trainerUserName + '\'' +
			", type=" + type +
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
		final TraineeTrainingsSearchRequest that = (TraineeTrainingsSearchRequest) o;
		if (!Objects.equals(traineeUserName, that.traineeUserName)) {
			return false;
		}
		if (!Objects.equals(periodFrom, that.periodFrom)) {
			return false;
		}
		if (!Objects.equals(periodTo, that.periodTo)) {
			return false;
		}
		if (!Objects.equals(trainerUserName, that.trainerUserName)) {
			return false;
		}
		return Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		int result = traineeUserName != null ? traineeUserName.hashCode() : 0;
		result = 31 * result + (periodFrom != null ? periodFrom.hashCode() : 0);
		result = 31 * result + (periodTo != null ? periodTo.hashCode() : 0);
		result = 31 * result + (trainerUserName != null ? trainerUserName.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		return result;
	}
}
