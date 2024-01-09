package com.epam.dmgolub.gym.repository.specification;

import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.repository.searchcriteria.TraineeTrainingsSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TraineeTrainingSpecification implements Specification<Training> {

	private final TraineeTrainingsSearchCriteria searchCriteria;

	public TraineeTrainingSpecification(final TraineeTrainingsSearchCriteria searchCriteria) {
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
			root.join("trainee").join("user").get("userName"),
			searchCriteria.getTraineeUserName()
		));
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
		if (searchCriteria.getTrainerUserName() != null && !searchCriteria.getTrainerUserName().isBlank()) {
			predicates.add(criteriaBuilder.equal(
				root.join("trainer").join("user").get("userName"),
				searchCriteria.getTrainerUserName()
			));
		}
		if (searchCriteria.getType() != null) {
			predicates.add(criteriaBuilder.equal(
				root.join("type").get("id"),
				searchCriteria.getType().getId()
			));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
