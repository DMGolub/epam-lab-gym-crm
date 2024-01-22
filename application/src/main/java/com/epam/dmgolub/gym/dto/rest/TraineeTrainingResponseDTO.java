package com.epam.dmgolub.gym.dto.rest;

import java.util.Date;
import java.util.Objects;

public class TraineeTrainingResponseDTO {

	private String name;
	private Date date;
	private TrainingTypeDTO type;
	private int duration;
	private String trainerUserName;

	public TraineeTrainingResponseDTO() {
		// Empty
	}

	public TraineeTrainingResponseDTO(
		final String name,
		final Date date,
		final TrainingTypeDTO type,
		final int duration,
		final String trainerUserName
	) {
		this.name = name;
		this.date = date;
		this.type = type;
		this.duration = duration;
		this.trainerUserName = trainerUserName;
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

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	@Override
	public String toString() {
		return "TraineeTrainingResponseDTO{" +
			"name='" + name + '\'' +
			", date=" + date +
			", type=" + type +
			", duration=" + duration +
			", trainerUserName='" + trainerUserName + '\'' +
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
		final TraineeTrainingResponseDTO that = (TraineeTrainingResponseDTO) o;
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
		return Objects.equals(trainerUserName, that.trainerUserName);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + duration;
		result = 31 * result + (trainerUserName != null ? trainerUserName.hashCode() : 0);
		return result;
	}
}
