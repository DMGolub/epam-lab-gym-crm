package com.epam.dmgolub.gym.dto;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class ChangePasswordRequestDTO {

	@NotBlank(message = "{userName.notBlank.violation}")
	private String userName;
	@NotBlank(message = "{password.notBlank.violation}")
	private String oldPassword;
	@NotBlank(message = "{password.notBlank.violation}")
	private String newPassword;

	public ChangePasswordRequestDTO() {
		// Empty
	}

	public ChangePasswordRequestDTO(final String userName, final String oldPassword, final String newPassword) {
		this.userName = userName;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(final String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePasswordRequestDTO{" + "userName='" + userName + '\'' + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final ChangePasswordRequestDTO that = (ChangePasswordRequestDTO) o;
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		if (!Objects.equals(oldPassword, that.oldPassword)) {
			return false;
		}
		return Objects.equals(newPassword, that.newPassword);
	}

	@Override
	public int hashCode() {
		int result = userName != null ? userName.hashCode() : 0;
		result = 31 * result + (oldPassword != null ? oldPassword.hashCode() : 0);
		result = 31 * result + (newPassword != null ? newPassword.hashCode() : 0);
		return result;
	}
}
