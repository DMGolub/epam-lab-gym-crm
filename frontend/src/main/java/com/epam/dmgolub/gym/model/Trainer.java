package com.epam.dmgolub.gym.model;

import java.util.Objects;

public class Trainer {

	private String firstName;
	private String lastName;
	private String userName;
	private boolean isActive;
	private String specialization;

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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final String specialization) {
		this.specialization = specialization;
	}

	@Override
	public String toString() {
		return "Trainer{" +
			"firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' +
			", isActive=" + isActive +
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
		if (isActive != trainer.isActive) {
			return false;
		}
		if (!Objects.equals(firstName, trainer.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, trainer.lastName)) {
			return false;
		}
		if (!Objects.equals(userName, trainer.userName)) {
			return false;
		}
		return Objects.equals(specialization, trainer.specialization);
	}

	@Override
	public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (userName != null ? userName.hashCode() : 0);
		result = 31 * result + (isActive ? 1 : 0);
		result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
		return result;
	}
}
