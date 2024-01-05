package com.epam.dmgolub.gym.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import java.util.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Trainee implements BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private Date dateOfBirth;
	private String address;
	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Trainee() {
		user = new User();
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
		user = new User(userId, firstName, lastName, userName, password, isActive);
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

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
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
		return "Trainee{id=" + id + ", firstName='" + user.getFirstName() + '\'' + ", lastName='" + user.getLastName() + '\'' +
			", userName='" + user.getUserName() + '\'' + ", isActive=" + user.isActive() + ", userId=" + user.getId() +
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
