package com.epam.dmgolub.gym.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Training implements BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@ManyToOne
	private Trainee trainee;
	@ManyToOne
	private Trainer trainer;
	@Column(nullable = false)
	private String name;
	@ManyToOne
	private TrainingType type;
	@Column(nullable = false)
	private Date date;
	@Column(nullable = false)
	private int duration;

	public Training() {
		// Empty
	}

	public Training(
		final Long id,
		final Trainee trainee,
		final Trainer trainer,
		final String name,
		final TrainingType type,
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

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public Trainee getTrainee() {
		return trainee;
	}

	public void setTrainee(final Trainee trainee) {
		this.trainee = trainee;
	}

	public Trainer getTrainer() {
		return trainer;
	}

	public void setTrainer(final Trainer trainer) {
		this.trainer = trainer;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TrainingType getType() {
		return type;
	}

	public void setType(final TrainingType type) {
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
		return "Training{id=" + id + ", trainee=" + trainee + ", trainer=" + trainer +
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
		final Training training = (Training) o;
		if (duration != training.duration) {
			return false;
		}
		if (!Objects.equals(id, training.id)) {
			return false;
		}
		if (!Objects.equals(trainee, training.trainee)) {
			return false;
		}
		if (!Objects.equals(trainer, training.trainer)) {
			return false;
		}
		if (!Objects.equals(name, training.name)) {
			return false;
		}
		if (!Objects.equals(type, training.type)) {
			return false;
		}
		return Objects.equals(date, training.date);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
