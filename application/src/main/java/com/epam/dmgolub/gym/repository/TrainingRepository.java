package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
	// Empty
}
