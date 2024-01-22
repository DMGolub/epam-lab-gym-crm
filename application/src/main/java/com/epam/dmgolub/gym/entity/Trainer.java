package com.epam.dmgolub.gym.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Trainer implements BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@ManyToOne
	private TrainingType specialization;
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;
	@ManyToMany(mappedBy = "trainers", fetch = EAGER)
	private List<Trainee> trainees;

	public Trainer() {
		user = new User();
		trainees = new ArrayList<>();
	}

	public Trainer(
		final Long id,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive,
		final Long userId,
		final TrainingType specialization,
		final List<Trainee> trainees
	) {
		user = new User(userId, firstName, lastName, userName, password, isActive);
		this.id = id;
		this.specialization = specialization;
		this.trainees = trainees;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public TrainingType getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingType specialization) {
		this.specialization = specialization;
	}

	public List<Trainee> getTrainees() {
		return trainees;
	}

	public void setTrainees(final List<Trainee> trainees) {
		this.trainees = trainees;
	}

	@Override
	public String toString() {
		return "Trainer{id=" + id + ", firstName='" + user.getFirstName() + '\'' + ", lastName='" + user.getLastName() + '\'' +
			", userName='" + user.getUserName() + '\'' + ", isActive=" + user.isActive() + ", userId=" + user.getId() +
			", specialization=" + specialization + "}";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Trainer trainer = (Trainer) o;
		if (!Objects.equals(id, trainer.id)) {
			return false;
		}
		return Objects.equals(specialization, trainer.specialization);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
