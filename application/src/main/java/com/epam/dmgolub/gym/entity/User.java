package com.epam.dmgolub.gym.entity;

public abstract class User implements BaseEntity<Long> {

	private Long id;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private boolean isActive;

	protected User() {
		// Empty
	}

	protected User(
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
		return "User{id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' +
			", userName='" + userName + '\'' + ", password='" + password + '\'' + ", isActive=" + isActive + '}';
	}
}
