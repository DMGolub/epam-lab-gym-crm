package com.epam.dmgolub.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.FIRST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.LAST_NAME_PATTERN_REGEXP;

public class TrainerRequestDTO {

	@NotBlank(message = "{firstName.notBlank.violation}")
	@Pattern(regexp = FIRST_NAME_PATTERN_REGEXP, message = "{firstName.pattern.violation}")
	private String firstName;
	@NotBlank(message = "{lastName.notBlank.violation}")
	@Pattern(regexp = LAST_NAME_PATTERN_REGEXP, message = "{lastName.pattern.violation}")
	private String lastName;
	private String userName;
	private boolean isActive;
	@NotNull(message = "{trainer.specialization.notNull.violation}")
	private TrainingTypeDTO specialization;

	public TrainerRequestDTO() {
		// Empty
	}

	public TrainerRequestDTO(
		final String firstName,
		final String lastName,
		final String userName,
		final boolean isActive,
		final TrainingTypeDTO specialization
	) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.isActive = isActive;
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

	public TrainingTypeDTO getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final TrainingTypeDTO specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "TrainerRequestDTO{firstName='" + firstName + '\'' +
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
		final TrainerRequestDTO that = (TrainerRequestDTO) o;
		if (isActive != that.isActive) {
			return false;
		}
		if (!Objects.equals(firstName, that.firstName)) {
			return false;
		}
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		if (!Objects.equals(lastName, that.lastName)) {
			return false;
		}
		return Objects.equals(specialization, that.specialization);
	}

	@Override
	public int hashCode() {
		return userName != null ? userName.hashCode() : 0;
	}
}
