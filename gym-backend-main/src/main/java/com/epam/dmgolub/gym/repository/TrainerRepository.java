package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

	Optional<Trainer> findByUserUserName(String userName);

	@Query("FROM Trainer t WHERE t.user.isActive=true AND t.id IN " +
		"(SELECT t.id FROM Trainer t JOIN t.trainees tr WHERE tr.user.userName = :traineeUserName)")
	List<Trainer> findActiveTrainersAssignedOnTrainee(@Param("traineeUserName") String userName);

	@Query("FROM Trainer t WHERE t.user.isActive=true AND t.id NOT IN " +
		"(SELECT t.id FROM Trainer t JOIN t.trainees tr WHERE tr.user.userName = :traineeUserName)")
	List<Trainer> findActiveTrainersNotAssignedOnTrainee(@Param("traineeUserName") String userName);
}
