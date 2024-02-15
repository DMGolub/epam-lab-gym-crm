package com.epam.dmgolub.gym.dto;

import java.util.Objects;

public class TrainingTypeDTO {

	private String name;
	private Long id;

	public TrainingTypeDTO() {
		// Empty
	}

	public TrainingTypeDTO(final String name, final Long id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	@Override
	public String
	toString() {
		return "TrainingTypeDTO{name='" + name + "', id=" + id + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainingTypeDTO that = (TrainingTypeDTO) o;
		if (!Objects.equals(name, that.name)) {
			return false;
		}
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return  id != null ? id.hashCode() : 0;
	}
}
