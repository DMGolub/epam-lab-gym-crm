package com.epam.dmgolub.gym.dto;

import java.util.Date;
import java.util.Objects;

public class TrainerTrainingResponseDTO {

	private String name;
	private Date date;
	private TrainingTypeDTO type;
	private int duration;
	private String traineeUserName;

	public TrainerTrainingResponseDTO() {
		// Empty
	}

	public TrainerTrainingResponseDTO(
		final String name,
		final Date date,
		final TrainingTypeDTO type,
		final int duration,
		final String traineeUserName
	) {
		this.name = name;
		this.date = date;
		this.type = type;
		this.duration = duration;
		this.traineeUserName = traineeUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public TrainingTypeDTO getType() {
		return type;
	}

	public void setType(final TrainingTypeDTO type) {
		this.type = type;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public String getTraineeUserName() {
		return traineeUserName;
	}

	public void setTraineeUserName(final String traineeUserName) {
		this.traineeUserName = traineeUserName;
	}

	@Override
	public String toString() {
		return "TrainerTrainingResponseDTO{" +
			"name='" + name + '\'' +
			", date=" + date +
			", type=" + type +
			", duration=" + duration +
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

		final TrainerTrainingResponseDTO that = (TrainerTrainingResponseDTO) o;

		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(name, that.name)) {
			return false;
		}
		if (!Objects.equals(date, that.date)) {
			return false;
		}
		if (!Objects.equals(type, that.type)) {
			return false;
		}
		return Objects.equals(traineeUserName, that.traineeUserName);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + duration;
		result = 31 * result + (traineeUserName != null ? traineeUserName.hashCode() : 0);
		return result;
	}
}
