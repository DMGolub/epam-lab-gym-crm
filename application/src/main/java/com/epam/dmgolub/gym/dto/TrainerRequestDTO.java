package com.epam.dmgolub.gym.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.FIRST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.LAST_NAME_PATTERN_REGEXP;

public class TrainerRequestDTO {

	private Long userId;
	@NotBlank(message = "{firstName.not.blank.violation}")
	@Pattern(regexp = FIRST_NAME_PATTERN_REGEXP, message = "{firstName.pattern.violation}")
	private String firstName;
	@NotBlank(message = "{lastName.not.blank.violation}")
	@Pattern(regexp = LAST_NAME_PATTERN_REGEXP, message = "{lastName.pattern.violation}")
	private String lastName;
	private boolean isActive;
	private Long id;
	@NotNull(message = "{trainer.specialization.notNull.violation}")
	private TrainingTypeDTO specialization;

	public TrainerRequestDTO() {
		// Empty
	}

	public TrainerRequestDTO(
		final Long userId,
		final String firstName,
		final String lastName,
		final boolean isActive,
		final Long id,
		final TrainingTypeDTO specialization
	) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
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
		return "TrainerRequestDTO{userId=" + userId + ", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' + ", isActive=" + isActive + ", id=" + id +
			", specialization=" + specialization + '}';
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
		if (!Objects.equals(userId, that.userId)) {
			return false;
		}
		if (!Objects.equals(firstName, that.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, that.lastName)) {
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
