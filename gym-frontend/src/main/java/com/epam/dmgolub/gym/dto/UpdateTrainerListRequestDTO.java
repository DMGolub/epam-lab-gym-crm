package com.epam.dmgolub.gym.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdateTrainerListRequestDTO {

	private String traineeUserName;
	private List<String> trainerUserNames;

	public UpdateTrainerListRequestDTO() {
		trainerUserNames = new ArrayList<>();
	}

	public UpdateTrainerListRequestDTO(final String traineeUserName, final List<String> trainerUserNames) {
		this.traineeUserName = traineeUserName;
		this.trainerUserNames = trainerUserNames;
	}

	public String getTraineeUserName() {
		return traineeUserName;
	}

	public void setTraineeUserName(final String traineeUserName) {
		this.traineeUserName = traineeUserName;
	}

	public List<String> getTrainerUserNames() {
		return trainerUserNames;
	}

	public void setTrainerUserNames(final List<String> trainerUserNames) {
		this.trainerUserNames = trainerUserNames;
	}

	@Override
	public String toString() {
		return "UpdateTrainerListRequestDTO{" +
			"traineeUserName='" + traineeUserName + '\'' +
			", trainerUserNames=" + trainerUserNames +
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
		final UpdateTrainerListRequestDTO that = (UpdateTrainerListRequestDTO) o;
		if (!Objects.equals(traineeUserName, that.traineeUserName)) {
			return false;
		}
		return Objects.equals(trainerUserNames, that.trainerUserNames);
	}

	@Override
	public int hashCode() {
		int result = traineeUserName != null ? traineeUserName.hashCode() : 0;
		result = 31 * result + (trainerUserNames != null ? trainerUserNames.hashCode() : 0);
		return result;
	}
}
