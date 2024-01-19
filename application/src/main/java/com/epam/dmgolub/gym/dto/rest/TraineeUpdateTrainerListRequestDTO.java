package com.epam.dmgolub.gym.dto.rest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.USERNAME_PATTERN_REGEXP;

public class TraineeUpdateTrainerListRequestDTO {

	@NotBlank(message = "{trainee.userName.notBlank.violation}")
	@Pattern(regexp = USERNAME_PATTERN_REGEXP, message = "{userName.pattern.violation}")
	private String traineeUserName;
	@NotNull(message = "{trainer.list.notNull.violation}")
	private List<String> trainerUserNames;

	public TraineeUpdateTrainerListRequestDTO() {
		trainerUserNames = new ArrayList<>();
	}

	public TraineeUpdateTrainerListRequestDTO(final String traineeUserName, final List<String> trainerUserNames) {
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
		return "TraineeUpdateTrainerListRequestDTO{" +
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
		final TraineeUpdateTrainerListRequestDTO that = (TraineeUpdateTrainerListRequestDTO) o;
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
