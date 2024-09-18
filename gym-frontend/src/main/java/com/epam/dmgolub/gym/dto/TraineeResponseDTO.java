package com.epam.dmgolub.gym.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TraineeResponseDTO {

	private String firstName;
	private String lastName;
	private String userName;
	private boolean isActive;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dateOfBirth;
	private String address;
	private List<TrainerDTO> trainers;

	public TraineeResponseDTO() {
		trainers = new ArrayList<>();
	}

	public TraineeResponseDTO(
		final String firstName,
		final String lastName,
		final String userName,
		final boolean isActive,
		final Date dateOfBirth,
		final String address,
		final List<TrainerDTO> trainers
	) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.isActive = isActive;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
		this.trainers = trainers;
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

	public List<TrainerDTO> getTrainers() {
		return trainers;
	}

	public void setTrainers(final List<TrainerDTO> trainers) {
		this.trainers = trainers;
	}

	@Override
	public String toString() {
		return "TraineeResponseDTO{firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' + ", userName='" + userName + '\'' +
			", isActive=" + isActive + ", dateOfBirth=" + dateOfBirth +
			", address='" + address + '\'' + ", trainers=" + trainers + '}';
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
		if (isActive != that.isActive) {
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
		return userName != null ? userName.hashCode() : 0;
	}

	public static class TrainerDTO {

		private String userName;
		private String firstName;
		private String lastName;
		private TrainingTypeDTO specialization;

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

		public TrainingTypeDTO getSpecialization() {
			return specialization;
		}

		public void setSpecialization(final TrainingTypeDTO specialization) {
			this.specialization = specialization;
		}

		@Override
		public String toString() {
			return "Trainer{userName='" + userName + '\'' + ", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' + ", specialization=" + specialization + '}';
		}
	}
}
