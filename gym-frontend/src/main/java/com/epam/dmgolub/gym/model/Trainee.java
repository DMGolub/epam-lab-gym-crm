package com.epam.dmgolub.gym.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Trainee {

	private String firstName;
	private String lastName;
	private String userName;
	private boolean isActive;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dateOfBirth;
	private String address;
	private List<Trainee.Trainer> trainers;

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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
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

	public List<Trainer> getTrainers() {
		return trainers;
	}

	public void setTrainers(final List<Trainer> trainers) {
		this.trainers = trainers;
	}

	@Override
	public String toString() {
		return "Trainee{" +
			"firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' +
			", isActive=" + isActive +
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
		final Trainee trainee = (Trainee) o;
		if (isActive != trainee.isActive) {
			return false;
		}
		if (!Objects.equals(firstName, trainee.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, trainee.lastName)) {
			return false;
		}
		if (!Objects.equals(userName, trainee.userName)) {
			return false;
		}
		if (!Objects.equals(dateOfBirth, trainee.dateOfBirth)) {
			return false;
		}
		if (!Objects.equals(address, trainee.address)) {
			return false;
		}
		return Objects.equals(trainers, trainee.trainers);
	}

	@Override
	public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (userName != null ? userName.hashCode() : 0);
		result = 31 * result + (isActive ? 1 : 0);
		result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (trainers != null ? trainers.hashCode() : 0);
		return result;
	}

	public static class Trainer {

		private String userName;
		private String firstName;
		private String lastName;
		private String specialization;

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

		public String getSpecialization() {
			return specialization;
		}

		public void setSpecialization(final String specialization) {
			this.specialization = specialization;
		}

		@Override
		public String toString() {
			return "Trainer{" +
				"userName='" + userName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", specialization='" + specialization + '\'' +
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
