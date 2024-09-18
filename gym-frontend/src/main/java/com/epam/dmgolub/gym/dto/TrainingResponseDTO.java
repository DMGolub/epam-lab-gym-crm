package com.epam.dmgolub.gym.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class TrainingResponseDTO {

	private TraineeResponseDTO trainee;
	private TrainerResponseDTO trainer;
	private String name;
	private TrainingTypeDTO type;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date;
	private int duration;

	public TrainingResponseDTO() {
		// Empty
	}

	public TrainingResponseDTO(
		final TraineeResponseDTO trainee,
		final TrainerResponseDTO trainer,
		final String name,
		final TrainingTypeDTO type,
		final Date date,
		final int duration
	) {
		this.trainee = trainee;
		this.trainer = trainer;
		this.name = name;
		this.type = type;
		this.date = date;
		this.duration = duration;
	}

	public TraineeResponseDTO getTrainee() {
		return trainee;
	}

	public void setTrainee(final TraineeResponseDTO trainee) {
		this.trainee = trainee;
	}

	public TrainerResponseDTO getTrainer() {
		return trainer;
	}

	public void setTrainer(final TrainerResponseDTO trainer) {
		this.trainer = trainer;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TrainingTypeDTO getType() {
		return type;
	}

	public void setType(final TrainingTypeDTO type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "TrainingResponseDTO{trainee=" + trainee + ", trainer=" + trainer +
			", name='" + name + '\'' + ", type=" + type + ", date=" + date + ", duration=" + duration + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainingResponseDTO that = (TrainingResponseDTO) o;
		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(trainee, that.trainee)) {
			return false;
		}
		if (!Objects.equals(trainer, that.trainer)) {
			return false;
		}
		if (!Objects.equals(name, that.name)) {
			return false;
		}
		if (!Objects.equals(type, that.type)) {
			return false;
		}
		return Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		int result = trainee != null ? trainee.hashCode() : 0;
		result = 31 * result + (trainer != null ? trainer.hashCode() : 0);
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + duration;
		return result;
	}
}
