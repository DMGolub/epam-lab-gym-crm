package com.epam.dmgolub.gym.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TraineeResponseDTO {

	private String userName;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String address;
	private boolean isActive;
	private List<TrainerDTO> trainers;

	public TraineeResponseDTO() {
		trainers = new ArrayList<>();
	}

	public TraineeResponseDTO(
		final String userName,
		final String firstName,
		final String lastName,
		final Date dateOfBirth,
		final String address,
		final boolean isActive,
		final List<TraineeResponseDTO.TrainerDTO> trainers
	) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.isActive = isActive;
		this.trainers = trainers;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	public List<TraineeResponseDTO.TrainerDTO> getTrainers() {
		return trainers;
	}

	public void setTrainers(final List<TraineeResponseDTO.TrainerDTO> trainers) {
		this.trainers = trainers;
	}

	@Override
	public String toString() {
		return "TraineeResponseDTO{" +
			"userName='" + userName + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", dateOfBirth=" + dateOfBirth +
			", address='" + address + '\'' +
			", isActive=" + isActive +
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
		final TraineeResponseDTO that = (TraineeResponseDTO) o;
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		if (isActive != that.isActive) {
			return false;
		}
		if (!Objects.equals(firstName, that.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, that.lastName)) {
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
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (isActive ? 1 : 0);
		result = 31 * result + (trainers != null ? trainers.hashCode() : 0);
		return result;
	}

	public static class TrainerDTO {

		private String userName;
		private String firstName;
		private String lastName;
		private String specialization;

		public TrainerDTO() {
			// Empty
		}

		public TrainerDTO(
			final String userName,
			final String firstName,
			final String lastName,
			final String specialization
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

		public String getSpecialization() {
			return specialization;
		}

		public void setSpecialization(final String specialization) {
			this.specialization = specialization;
		}

		@Override
		public String toString() {
			return "TrainerDTO{" +
				"userName='" + userName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", specialization=" + specialization +
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

			final TrainerDTO that = (TrainerDTO) o;

			if (!Objects.equals(userName, that.userName)) {
				return false;
			}
			if (!Objects.equals(firstName, that.firstName)) {
				return false;
			}
			if (!Objects.equals(lastName, that.lastName)) {
				return false;
			}
			return Objects.equals(specialization, that.specialization);
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
