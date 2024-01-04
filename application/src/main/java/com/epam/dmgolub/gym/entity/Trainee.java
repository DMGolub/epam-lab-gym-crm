package com.epam.dmgolub.gym.entity;

import java.util.Date;
import java.util.Objects;

public class Trainee extends User {

	private Long id;
	private Date dateOfBirth;
	private String address;

	public Trainee() {
		super();
	}

	public Trainee(
		final Long id,
		final String firstName,
		final String lastName,
		final String userName,
		final String password,
		final boolean isActive,
		final Long userId,
		final Date dateOfBirth,
		final String address
	) {
		super(userId, firstName, lastName, userName, password, isActive);
		this.id = id;
		this.dateOfBirth = dateOfBirth;
		this.address = address;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return super.getId();
	}

	public void setUserId(final Long id) {
		super.setId(id);
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

	@Override
	public String toString() {
		return "Trainee{id=" + id + ", firstName='" + getFirstName() + '\'' + ", lastName='" + getLastName() + '\'' +
			", userName='" + getUserName() + '\'' + ", isActive=" + isActive() + ", userId=" + getUserId() +
			", dateOfBirth=" + dateOfBirth + ", address='" + address + '\'' + "} ";
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Trainee trainee = (Trainee) o;
		if (!Objects.equals(id, trainee.id)) {
			return false;
		}
		if (!Objects.equals(dateOfBirth, trainee.dateOfBirth)) {
			return false;
		}
		return Objects.equals(address, trainee.address);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
