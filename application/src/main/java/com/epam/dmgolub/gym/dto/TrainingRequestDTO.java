package com.epam.dmgolub.gym.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINEE_ID_NOT_NULL_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINEE_ID_POSITIVE_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINER_ID_NOT_NULL_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINER_ID_POSITIVE_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_DATE_NOT_NULL_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_DURATION_POSITIVE_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_NAME_NOT_BLANK_MESSAGE;
import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_TYPE_NOT_NULL_MESSAGE;

public class TrainingRequestDTO {

	private Long id;
	@NotNull(message = TRAINEE_ID_NOT_NULL_MESSAGE)
	@Positive(message = TRAINEE_ID_POSITIVE_MESSAGE)
	private Long traineeId;
	@NotNull(message = TRAINER_ID_NOT_NULL_MESSAGE)
	@Positive(message = TRAINER_ID_POSITIVE_MESSAGE)
	private Long trainerId;
	@NotBlank(message = TRAINING_NAME_NOT_BLANK_MESSAGE)
	private String name;
	@NotNull(message = TRAINING_TYPE_NOT_NULL_MESSAGE)
	private TrainingTypeDTO type;
	@NotNull(message = TRAINING_DATE_NOT_NULL_MESSAGE)
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date;
	@Positive(message = TRAINING_DURATION_POSITIVE_MESSAGE)
	private int duration;

	public TrainingRequestDTO() {
		// Empty
	}

	public TrainingRequestDTO(
		final Long id,
		final Long traineeId,
		final Long trainerId,
		final String name,
		final TrainingTypeDTO type,
		final Date date,
		final int duration
	) {
		this.id = id;
		this.traineeId = traineeId;
		this.trainerId = trainerId;
		this.name = name;
		this.type = type;
		this.date = date;
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(final Long traineeId) {
		this.traineeId = traineeId;
	}

	public Long getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(final Long trainerId) {
		this.trainerId = trainerId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public TrainingTypeDTO getType() {
		return type;
	}

	public void setType(final TrainingTypeDTO type) {
		this.type = type;
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

	@Override
	public String toString() {
		return "TrainingRequestDTO{id=" + id + ", traineeId=" + traineeId + ", trainerId=" + trainerId +
			", name='" + name + '\'' + ", type=" + type + ", date=" + date + ", duration=" + duration + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainingRequestDTO that = (TrainingRequestDTO) o;
		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		if (!Objects.equals(traineeId, that.traineeId)) {
			return false;
		}
		if (!Objects.equals(trainerId, that.trainerId)) {
			return false;
		}
		if (!Objects.equals(name, that.name)) {
			return false;
		}
		if (!Objects.equals(type, that.type)) {
			return false;
		}
		return Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
