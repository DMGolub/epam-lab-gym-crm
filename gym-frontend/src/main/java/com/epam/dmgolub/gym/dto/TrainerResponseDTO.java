package com.epam.dmgolub.gym.dto;

import java.util.Objects;

public class TrainerResponseDTO {

	private String firstName;
	private String lastName;
	private String userName;
	private boolean isActive;
	private TrainingTypeDTO specialization;

	public TrainerResponseDTO() {
		// Empty
	}

	public TrainerResponseDTO(
		final String firstName,
		final String lastName,
		final String userName,
		final boolean isActive,
		final TrainingTypeDTO specialization
	) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = isActive;
		this.userName = userName;
		this.specialization = specialization;
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

	public TrainingTypeDTO getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingTypeDTO specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "TrainerResponseDTO{firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' +
			", isActive=" + isActive +
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
		final TrainerResponseDTO that = (TrainerResponseDTO) o;
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
		return Objects.equals(specialization, that.specialization);
	}

	@Override
	public int hashCode() {
		return userName != null ? userName.hashCode() : 0;
	}
}
