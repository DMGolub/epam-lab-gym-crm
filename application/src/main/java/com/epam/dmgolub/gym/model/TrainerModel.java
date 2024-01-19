package com.epam.dmgolub.gym.model;

import java.util.List;
import java.util.Objects;

public class TrainerModel {

	private Long userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private boolean isActive;
	private Long id;
	private TrainingTypeModel specialization;
	private List<TrainerModel.Trainee> trainees;

	public TrainerModel() {
		// Empty
	}

	public TrainerModel(
		final Long userId,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive,
		final Long id,
		final TrainingTypeModel specialization
	) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.isActive = isActive;
		this.id = id;
		this.specialization = specialization;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(final Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public TrainingTypeModel getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingTypeModel specialization) {
		this.specialization = specialization;
	}

	public List<Trainee> getTrainees() {
		return trainees;
	}

	public void setTrainees(final List<TrainerModel.Trainee> trainees) {
		this.trainees = trainees;
	}

	@Override
	public String toString() {
		return "TrainerModel{userId=" + userId + ", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' + ", userName='" + userName + '\'' + ", isActive=" + isActive +
			", id=" + id + ", specialization=" + specialization + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainerModel that = (TrainerModel) o;
		if (isActive != that.isActive) {
			return false;
		}
		if (!Objects.equals(userId, that.userId)) {
			return false;
		}
		if (!Objects.equals(firstName, that.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, that.lastName)) {
			return false;
		}
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		if (!Objects.equals(password, that.password)) {
			return false;
		}
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		return Objects.equals(specialization, that.specialization);
	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}

	public static class Trainee {

		private String userName;
		private String firstName;
		private String lastName;

		public Trainee() {
			// Empty
		}

		public Trainee(final String userName, final String firstName, final String lastName) {
			this.userName = userName;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(final String userName) {
			this.userName = userName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(final String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(final String lastName) {
			this.lastName = lastName;
		}

		@Override
		public String toString() {
			return "Trainee{" +
				"userName='" + userName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
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
			final Trainee trainee = (Trainee) o;
			if (!Objects.equals(userName, trainee.userName)) {
				return false;
			}
			if (!Objects.equals(firstName, trainee.firstName)) {
				return false;
			}
			return Objects.equals(lastName, trainee.lastName);
		}

		@Override
		public int hashCode() {
			int result = userName != null ? userName.hashCode() : 0;
			result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
			result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
			return result;
		}
	}
}
