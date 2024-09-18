package com.epam.dmgolub.gym.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TraineeModel {

	private Long userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private boolean isActive;
	private Long id;
	private Date dateOfBirth;
	private String address;
	private List<TraineeModel.Trainer> trainers;

	public TraineeModel() {
		trainers = new ArrayList<>();
	}

	public TraineeModel(
		final Long userId,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive,
		final Long id,
		final Date dateOfBirth,
		final String address,
		final List<TraineeModel.Trainer> trainers
	) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.isActive = isActive;
		this.id = id;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.trainers = trainers;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public List<TraineeModel.Trainer> getTrainers() {
		return trainers;
	}

	public void setTrainers(final List<TraineeModel.Trainer> trainers) {
		this.trainers = trainers;
	}

	@Override
	public String
	toString() {
		return "TraineeModel{userId=" + userId +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' +
			", isActive=" + isActive +
			", id=" + id +
			", dateOfBirth=" + dateOfBirth +
			", address='" + address + '\'' +
			", trainers=" + trainers +
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
		final TraineeModel that = (TraineeModel) o;
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
		if (!Objects.equals(dateOfBirth, that.dateOfBirth)) {
			return false;
		}
		if (!Objects.equals(address, that.address)) {
			return false;
		}
		return Objects.equals(trainers, that.trainers);
	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}

	public static class Trainer {

		private String userName;
		private String firstName;
		private String lastName;
		private TrainingTypeModel specialization;

		public Trainer() {
			// Empty
		}

		public Trainer(
			final String userName,
			final String firstName,
			final String lastName,
			final TrainingTypeModel specialization
		) {
			this.userName = userName;
			this.firstName = firstName;
			this.lastName = lastName;
			this.specialization = specialization;
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

		public TrainingTypeModel getSpecialization() {
			return specialization;
		}

		public void setSpecialization(final TrainingTypeModel specialization) {
			this.specialization = specialization;
		}

		@Override
		public String toString() {
			return "Trainer{userName='" + userName + '\'' + ", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' + ", specialization=" + specialization + '}';
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

			if (!Objects.equals(userName, trainer.userName)) {
				return false;
			}
			if (!Objects.equals(firstName, trainer.firstName)) {
				return false;
			}
			if (!Objects.equals(lastName, trainer.lastName)) {
				return false;
			}
			return Objects.equals(specialization, trainer.specialization);
		}

		@Override
		public int hashCode() {
			int result = userName != null ? userName.hashCode() : 0;
			result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
			result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
			result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
			return result;
		}
	}
}
