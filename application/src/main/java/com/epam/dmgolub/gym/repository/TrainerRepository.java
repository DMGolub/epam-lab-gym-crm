package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
	// Empty
}
