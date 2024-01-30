package com.epam.dmgolub.gym.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainerResponseDTO {

	private String userName;
	private String firstName;
	private String lastName;
	private String specialization;
	private boolean isActive;
	private List<TraineeDTO> trainees;

	public TrainerResponseDTO() {
		trainees = new ArrayList<>();
	}

	public TrainerResponseDTO(
		final String userName,
		final String firstName,
		final String lastName,
		final String specialization,
		final boolean isActive,
		final List<TrainerResponseDTO.TraineeDTO> trainees
	) {
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.specialization = specialization;
		this.isActive = isActive;
		this.trainees = trainees;
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

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(final String specialization) {
		this.specialization = specialization;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(final boolean active) {
		isActive = active;
	}

	public List<TrainerResponseDTO.TraineeDTO> getTrainees() {
		return trainees;
	}

	public void setTrainees(final List<TrainerResponseDTO.TraineeDTO> trainees) {
		this.trainees = trainees;
	}

	@Override
	public String toString() {
		return "TrainerResponseDTO{" +
			"userName='" + userName + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", specialization=" + specialization +
			", isActive=" + isActive +
			", trainees=" + trainees +
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
		final TrainerResponseDTO that = (TrainerResponseDTO) o;
		if (!Objects.equals(userName, that.userName)) {
			return false;
		}
		if (isActive != that.isActive) {
			return false;
		}
		if (!Objects.equals(firstName, that.firstName)) {
			return false;
		}
		if (!Objects.equals(lastName, that.lastName)) {
			return false;
		}
		if (!Objects.equals(specialization, that.specialization)) {
			return false;
		}
		return Objects.equals(trainees, that.trainees);
	}

	@Override
	public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (specialization != null ? specialization.hashCode() : 0);
		result = 31 * result + (isActive ? 1 : 0);
		result = 31 * result + (trainees != null ? trainees.hashCode() : 0);
		return result;
	}

	public static class TraineeDTO {

		private String userName;
		private String firstName;
		private String lastName;

		public TraineeDTO() {
			// Empty
		}

		public TraineeDTO(final String userName, final String firstName, final String lastName) {
			this.userName = userName;
			this.firstName = firstName;
			this.lastName = lastName;
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

		@Override
		public String toString() {
			return "TraineeDTO{" +
				"userName='" + userName + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
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
			final TraineeDTO that = (TraineeDTO) o;
			if (!Objects.equals(userName, that.userName)) {
				return false;
			}
			if (!Objects.equals(firstName, that.firstName)) {
				return false;
			}
			return Objects.equals(lastName, that.lastName);
		}

		@Override
		public int hashCode() {
			int result = userName != null ? userName.hashCode() : 0;
			result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
			result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
			return result;
		}
	}
}
