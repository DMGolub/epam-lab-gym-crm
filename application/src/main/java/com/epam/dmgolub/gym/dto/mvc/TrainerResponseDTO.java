package com.epam.dmgolub.gym.dto.mvc;

import java.util.Objects;

public class TrainerResponseDTO {

	private Long userId;
	private String firstName;
	private String lastName;
	private String userName;
	private boolean isActive;
	private Long id;
	private TrainingTypeDTO specialization;

	public TrainerResponseDTO() {
		// Empty
	}

	public TrainerResponseDTO(
		final Long userId,
		final String firstName,
		final String lastName,
		final String userName,
		final boolean isActive,
		final Long id,
		final TrainingTypeDTO specialization
	) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isActive = isActive;
		this.userName = userName;
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

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public TrainingTypeDTO getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingTypeDTO specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "TrainerResponseDTO{userId=" + userId + ", firstName='" + firstName + '\'' +
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
		final TrainerResponseDTO that = (TrainerResponseDTO) o;
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
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		return Objects.equals(specialization, that.specialization);
	}

	@Override
	public int hashCode() {
		return userId != null ? userId.hashCode() : 0;
	}
}
