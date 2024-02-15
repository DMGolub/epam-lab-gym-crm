package com.epam.dmgolub.gym.model;

import java.util.Objects;

public class UserModel implements BaseModel<Long> {

	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private boolean isActive;

	public UserModel() {
		// Empty
	}

	public UserModel(
		final Long id,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive
	) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
		this.isActive = isActive;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	@Override
	public String toString() {
		return "UserModel{id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' + ", isActive=" + isActive + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final UserModel user = (UserModel) o;
		if (isActive != user.isActive) {
			return false;
		}
		if (!Objects.equals(id, user.id)) {
			return false;
		}
		if (!Objects.equals(firstName, user.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, user.lastName)) {
			return false;
		}
		if (!Objects.equals(userName, user.userName)) {
			return false;
		}
		return Objects.equals(password, user.password);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
