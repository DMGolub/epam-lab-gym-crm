package com.epam.dmgolub.gym.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
public class TrainingType implements BaseEntity<Long>, Serializable {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;

	public TrainingType() {
		// Empty
	}

	public TrainingType(final Long id, final String name) {
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
		return "TrainingType{id=" + id + ", name='" + name + '\'' + '}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainingType that = (TrainingType) o;
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
