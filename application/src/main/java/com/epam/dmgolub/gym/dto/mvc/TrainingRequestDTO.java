package com.epam.dmgolub.gym.dto.mvc;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Objects;

import static com.epam.dmgolub.gym.dto.constant.Constants.TRAINING_NAME_PATTERN_REGEXP;
import static com.epam.dmgolub.gym.dto.constant.Constants.USERNAME_PATTERN_REGEXP;

public class TrainingRequestDTO {

	private Long id;
	@NotBlank(message = "{trainee.userName.notBlank.violation}")
	@Pattern(regexp = USERNAME_PATTERN_REGEXP, message = "{userName.pattern.violation}")
	private String traineeUserName;
	@NotBlank(message = "{trainer.userName.notBlank.violation}")
	@Pattern(regexp = USERNAME_PATTERN_REGEXP, message = "{userName.pattern.violation}")
	private String trainerUserName;
	@NotBlank(message = "{training.name.notBlank.violation}")
	@Pattern(regexp = TRAINING_NAME_PATTERN_REGEXP, message = "{training.name.pattern.violation}")
	private String name;
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
		final String traineeUserName,
		final String trainerUserName,
		final String name,
		final Date date,
		final int duration
	) {
		this.id = id;
		this.traineeUserName = traineeUserName;
		this.trainerUserName = trainerUserName;
		this.name = name;
		this.date = date;
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTraineeUserName() {
		return traineeUserName;
	}

	public void setTraineeUserName(final String traineeUserName) {
		this.traineeUserName = traineeUserName;
	}

	public String getTrainerUserName() {
		return trainerUserName;
	}

	public void setTrainerUserName(final String trainerUserName) {
		this.trainerUserName = trainerUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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
		return "TrainingRequestDTO{id=" + id +
			", traineeUserName='" + traineeUserName + '\'' +
			", trainerUserName='" + trainerUserName + '\'' +
			", name='" + name + '\'' +
			", date=" + date +
			", duration=" + duration +
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
		final TrainingRequestDTO that = (TrainingRequestDTO) o;
		if (duration != that.duration) {
			return false;
		}
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		if (!Objects.equals(traineeUserName, that.traineeUserName)) {
			return false;
		}
		if (!Objects.equals(trainerUserName, that.trainerUserName)) {
			return false;
		}
		if (!Objects.equals(name, that.name)) {
			return false;
		}
		return Objects.equals(date, that.date);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
