package com.epam.dmgolub.gym.model;

import java.util.Date;
import java.util.Objects;

public class TrainingModel {

	private Long id;
	private TraineeModel trainee;
	private TrainerModel trainer;
	private String name;
	private TrainingTypeModel type;
	private Date date;
	private int duration;

	public TrainingModel() {
		// Empty
	}

	public TrainingModel(
		final Long id,
		final TraineeModel trainee,
		final TrainerModel trainer,
		final String name,
		final TrainingTypeModel type,
		final Date date,
		final int duration
	) {
		this.id = id;
		this.trainee = trainee;
		this.trainer = trainer;
		this.name = name;
		this.type = type;
		this.date = date;
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public TraineeModel getTrainee() {
		return trainee;
	}

	public void setTrainee(final TraineeModel trainee) {
		this.trainee = trainee;
	}

	public TrainerModel getTrainer() {
		return trainer;
	}

	public void setTrainer(final TrainerModel trainer) {
		this.trainer = trainer;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TrainingTypeModel getType() {
		return type;
	}

	public void setType(final TrainingTypeModel type) {
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
		return "TrainingModel{id=" + id + ", trainee=" + trainee + ", trainer=" + trainer +
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
		final TrainingModel that = (TrainingModel) o;
		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(id, that.id)) {
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
		return id != null ? id.hashCode() : 0;
	}
}
