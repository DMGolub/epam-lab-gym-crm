package com.epam.dmgolub.gym.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

public class LoginRequestDTO implements Serializable {

	@NotBlank(message = "{userName.notBlank.violation}")
	private String userName;
	@NotBlank(message = "{password.notBlank.violation}")
	private String password;

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
		return "LoginRequestDTO{" + "userName='" + userName + '\'' + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final LoginRequestDTO that = (LoginRequestDTO) o;
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
