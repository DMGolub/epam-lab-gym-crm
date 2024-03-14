package com.epam.dmgolub.gym.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

public class WorkloadUpdateRequestDTO {

	private String trainerUserName;
	private String trainerFirstName;
	private String trainerLastName;
	private Boolean isActive;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date;
	private int duration;
	private String actionType;

	public WorkloadUpdateRequestDTO() {
		// Empty
	}

	public WorkloadUpdateRequestDTO(
		final String trainerUserName,
		final String trainerFirstName,
		final String trainerLastName,
		final Boolean isActive,
		final Date date,
		final int duration,
		final String actionType
	) {
		this.trainerUserName = trainerUserName;
		this.trainerFirstName = trainerFirstName;
		this.trainerLastName = trainerLastName;
		this.isActive = isActive;
		this.date = date;
		this.duration = duration;
		this.actionType = actionType;
	}

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	public String getTrainerFirstName() {
		return trainerFirstName;
	}

	public void setTrainerFirstName(final String trainerFirstName) {
		this.trainerFirstName = trainerFirstName;
	}

	public String getTrainerLastName() {
		return trainerLastName;
	}

	public void setTrainerLastName(final String trainerLastName) {
		this.trainerLastName = trainerLastName;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(final Boolean active) {
		isActive = active;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(final String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "TrainingRequestDTO{" +
			"trainerUserName='" + trainerUserName + '\'' +
			", trainerFirstName='" + trainerFirstName + '\'' +
			", trainerLastName='" + trainerLastName + '\'' +
			", isActive=" + isActive +
			", date=" + date +
			", duration=" + duration +
			", actionType='" + actionType + '\'' +
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
		final WorkloadUpdateRequestDTO that = (WorkloadUpdateRequestDTO) o;
		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(trainerUserName, that.trainerUserName)) {
			return false;
		}
		if (!Objects.equals(trainerFirstName, that.trainerFirstName)) {
			return false;
		}
		if (!Objects.equals(trainerLastName, that.trainerLastName)) {
			return false;
		}
		if (!Objects.equals(isActive, that.isActive)) {
			return false;
		}
		if (!Objects.equals(date, that.date)) {
			return false;
		}
		return Objects.equals(actionType, that.actionType);
	}

	@Override
	public int hashCode() {
		int result = trainerUserName != null ? trainerUserName.hashCode() : 0;
		result = 31 * result + (trainerFirstName != null ? trainerFirstName.hashCode() : 0);
		result = 31 * result + (trainerLastName != null ? trainerLastName.hashCode() : 0);
		result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
		result = 31 * result + (date != null ? date.hashCode() : 0);
		result = 31 * result + duration;
		result = 31 * result + (actionType != null ? actionType.hashCode() : 0);
		return result;
	}
}
