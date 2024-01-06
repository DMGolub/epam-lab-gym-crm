package com.epam.dmgolub.gym.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_NAME_PATTERN_REGEXP;

public class TrainingRequestDTO {

	private Long id;
	@NotNull(message = "{trainee.id.notNull.violation}")
	@Positive(message = "{trainee.id.positive.violation}")
	private Long traineeId;
	@NotNull(message = "{trainer.id.notNull.violation}")
	@Positive(message = "{trainer.id.positive.violation}")
	private Long trainerId;
	@NotBlank(message = "{training.name.notBlank.violation}")
	@Pattern(regexp = TRAINING_NAME_PATTERN_REGEXP, message = "{training.name.pattern.violation}")
	private String name;
	@NotNull(message = "{training.type.notNull.violation}")
	private TrainingTypeDTO type;
	@NotNull(message = "{training.date.notNull.violation}")
	@Future(message = "{training.date.future.violation}")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private Date date;
	@Min(value = 30, message = "{training.date.duration.violation}")
	@Max(value = 480, message = "{training.date.duration.violation}")
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
