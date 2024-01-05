package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
	// Empty
}
