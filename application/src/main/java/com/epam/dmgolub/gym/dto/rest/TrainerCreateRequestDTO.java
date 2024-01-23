package com.epam.dmgolub.gym.dto.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.FIRST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.LAST_NAME_PATTERN_REGEXP;

public class TrainerCreateRequestDTO {

	@NotBlank(message = "{firstName.notBlank.violation}")
	@Pattern(regexp = FIRST_NAME_PATTERN_REGEXP, message = "{firstName.pattern.violation}")
	private String firstName;
	@NotBlank(message = "{lastName.notBlank.violation}")
	@Pattern(regexp = LAST_NAME_PATTERN_REGEXP, message = "{lastName.pattern.violation}")
	private String lastName;
	@NotBlank(message = "{trainer.specialization.notBlank.violation}")
	private String specialization;

	public TrainerCreateRequestDTO() {
		// Empty
	}

	public TrainerCreateRequestDTO(
		final String firstName,
		final String lastName,
		final String specialization
	) {
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final String specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "TrainerCreateRequestDTO{" +
			"firstName='" + firstName + '\'' +
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

		final TrainerCreateRequestDTO that = (TrainerCreateRequestDTO) o;

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
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
		return result;
	}
}
