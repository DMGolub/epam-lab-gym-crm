package com.epam.dmgolub.gym.model;

import java.util.Objects;

public class Credentials {

	private String userName;
	private String password;

	public Credentials() {
		// Empty
	}

	public Credentials(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
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

	@Override
	public String toString() {
		return "Credentials{" + "userName='" + userName + '\'' + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Credentials that = (Credentials) o;
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		return Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (password != null ? password.hashCode() : 0);
		return result;
	}
}
