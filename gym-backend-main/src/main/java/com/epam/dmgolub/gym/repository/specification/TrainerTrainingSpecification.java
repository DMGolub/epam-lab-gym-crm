package com.epam.dmgolub.gym.repository.specification;

import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.repository.searchcriteria.TrainerTrainingsSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainerTrainingSpecification implements Specification<Training> {

	private final TrainerTrainingsSearchCriteria searchCriteria;

	public TrainerTrainingSpecification(final TrainerTrainingsSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
	public Predicate toPredicate(
		final Root<Training> root,
		final @NonNull CriteriaQuery<?> query,
		final CriteriaBuilder criteriaBuilder
	) {
		final List<Predicate> predicates = 	new ArrayList<>();
		predicates.add(criteriaBuilder.equal(
			root.join("trainer").join("user").get("userName"),
			searchCriteria.getTrainerUserName())
		);
		if (searchCriteria.getPeriodFrom() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(
				root.get("date"),
				searchCriteria.getPeriodFrom()
			));
		}
		if (searchCriteria.getPeriodTo() != null) {
			predicates.add(criteriaBuilder.lessThanOrEqualTo(
				root.get("date"),
				searchCriteria.getPeriodTo()
			));
		}
		if (searchCriteria.getTraineeUserName() != null && !searchCriteria.getTraineeUserName().isBlank()) {
			predicates.add(criteriaBuilder.equal(
				root.join("trainee").join("user").get("userName"),
				searchCriteria.getTraineeUserName()
			));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final TrainerTrainingSpecification that = (TrainerTrainingSpecification) o;
		return Objects.equals(searchCriteria, that.searchCriteria);
	}

	@Override
	public int hashCode() {
		return searchCriteria != null ? searchCriteria.hashCode() : 0;
	}
}
