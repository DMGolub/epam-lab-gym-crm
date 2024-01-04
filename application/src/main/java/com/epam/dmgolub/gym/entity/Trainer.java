package com.epam.dmgolub.gym.entity;

import java.util.Objects;

public class Trainer extends User {

	private Long id;
	private TrainingType specialization;

	public Trainer() {
		super();
	}

	public Trainer(
		final Long id,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive,
		final Long userId,
		final TrainingType specialization
	) {
		super(userId, firstName, lastName, userName, password, isActive);
		this.id = id;
		this.specialization = specialization;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return super.getId();
	}

	public void setUserId(final Long id) {
		super.setId(id);
	}

	public TrainingType getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingType specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "Trainer{id=" + id + ", firstName='" + getFirstName() + '\'' + ", lastName='" + getLastName() + '\'' +
			", userName='" + getUserName() + '\'' + ", isActive=" + isActive() + ", userId=" + getUserId() +
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
