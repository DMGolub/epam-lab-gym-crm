package com.epam.dmgolub.gym.model;

import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class Training {

	private String name;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date;
	private TrainingTypeDTO type;
	private int duration;
	private String traineeUserName;
	private String trainerUserName;

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

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	@Override
	public String toString() {
		return "Training{" +
			"name='" + name + '\'' +
			", date=" + date +
			", type=" + type +
			", duration=" + duration +
			", traineeUserName='" + traineeUserName + '\'' +
			", trainerUserName=" + trainerUserName +
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
		final Training training = (Training) o;
		if (duration != training.duration) {
			return false;
		}
		if (!Objects.equals(name, training.name)) {
			return false;
		}
		if (!Objects.equals(date, training.date)) {
			return false;
		}
		if (!Objects.equals(type, training.type)) {
			return false;
		}
		if (!Objects.equals(traineeUserName, training.traineeUserName)) {
			return false;
		}
		return Objects.equals(trainerUserName, training.trainerUserName);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + duration;
		result = 31 * result + (traineeUserName != null ? traineeUserName.hashCode() : 0);
		result = 31 * result + (trainerUserName != null ? trainerUserName.hashCode() : 0);
		return result;
	}
}
