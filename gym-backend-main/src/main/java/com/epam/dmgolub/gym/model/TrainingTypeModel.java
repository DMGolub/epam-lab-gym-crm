package com.epam.dmgolub.gym.model;

import java.io.Serializable;
import java.util.Objects;

public class TrainingTypeModel implements BaseModel<Long>, Serializable {

	private Long id;
	private String name;

	public TrainingTypeModel() {
		// Empty
	}

	public TrainingTypeModel(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
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
	public String toString() {
		return "TrainingTypeModel{id=" + id + ", name='" + name + '\'' + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainingTypeModel that = (TrainingTypeModel) o;
		if (!Objects.equals(id, that.id)) {
			return false;
		}
		return Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}
