package com.epam.dmgolub.gym.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.FIRST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.LAST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.USERNAME_PATTERN_REGEXP;

public class TrainerUpdateRequestDTO {

	@NotBlank(message = "{trainer.userName.notBlank.violation}")
	@Pattern(regexp = USERNAME_PATTERN_REGEXP, message = "{userName.pattern.violation}")
	private String userName;
	@NotBlank(message = "{firstName.notBlank.violation}")
	@Pattern(regexp = FIRST_NAME_PATTERN_REGEXP, message = "{firstName.pattern.violation}")
	private String firstName;
	@NotBlank(message = "{lastName.notBlank.violation}")
	@Pattern(regexp = LAST_NAME_PATTERN_REGEXP, message = "{lastName.pattern.violation}")
	private String lastName;
	@NotBlank(message = "{trainer.specialization.notBlank.violation}")
	private String specialization;
	@NotNull(message = "{trainer.isActive.notNull.violation}")
	private Boolean isActive;

	public TrainerUpdateRequestDTO() {
		// Empty
	}

	public TrainerUpdateRequestDTO(
		final String userName,
		final String firstName,
		final String lastName,
		final String specialization,
		final boolean isActive
	) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.specialization = specialization;
		this.isActive = isActive;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	@Override
	public String toString() {
		return "TrainerUpdateRequestDTO{" +
			"userName='" + userName + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", specialization=" + specialization +
			", isActive=" + isActive +
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
		final TrainerUpdateRequestDTO that = (TrainerUpdateRequestDTO) o;
		if (isActive != that.isActive) {
			return false;
		}
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
		result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
		return result;
	}
}
