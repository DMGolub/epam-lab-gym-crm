package com.epam.dmgolub.gym.dto;

import com.epam.dmgolub.gym.validation.annotation.MinimumAgeLimit;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.FIRST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.LAST_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.USERNAME_PATTERN_REGEXP;

public class TraineeUpdateRequestDTO {

	@NotBlank(message = "{trainee.userName.notBlank.violation}")
	@Pattern(regexp = USERNAME_PATTERN_REGEXP, message = "{userName.pattern.violation}")
	private String userName;
	@NotBlank(message = "{firstName.notBlank.violation}")
	@Pattern(regexp = FIRST_NAME_PATTERN_REGEXP, message = "{firstName.pattern.violation}")
	private String firstName;
	@NotBlank(message = "{lastName.notBlank.violation}")
	@Pattern(regexp = LAST_NAME_PATTERN_REGEXP, message = "{lastName.pattern.violation}")
	private String lastName;
	@MinimumAgeLimit(value = 7, message = "{trainee.minimum.age.violation}")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date dateOfBirth;
	private String address;
	@NotNull(message = "{trainee.isActive.notNull.violation}")
	private Boolean isActive;

	public TraineeUpdateRequestDTO() {
		// Empty
	}

	public TraineeUpdateRequestDTO(
		final String userName,
		final String firstName,
		final String lastName,
		final Date dateOfBirth,
		final String address,
		final boolean isActive
	) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
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

	@Override
	public String toString() {
		return "TraineeUpdateRequestDTO{" +
			"userName='" + userName + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", dateOfBirth=" + dateOfBirth +
			", address='" + address + '\'' +
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
		final TraineeUpdateRequestDTO that = (TraineeUpdateRequestDTO) o;
		if (!Objects.equals(userName, that.userName)) {
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
		return Objects.equals(isActive, that.isActive);
	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
		result = 31 * result + (address != null ? address.hashCode() : 0);
		result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
		return result;
	}
}
