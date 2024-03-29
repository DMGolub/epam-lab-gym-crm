package com.epam.dmgolub.gym.dto.mvc;

import java.util.Objects;

public class TrainingTypeDTO {

	private Long id;
	private String name;

	public TrainingTypeDTO() {
		// Empty
	}

	public TrainingTypeDTO(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String
	toString() {
		return "TrainingTypeDTO{id=" + id + ", name='" + name + '\'' + '}';
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
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return  id != null ? id.hashCode() : 0;
	}
}
