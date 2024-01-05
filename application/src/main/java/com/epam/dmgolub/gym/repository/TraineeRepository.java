package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {
	// empty
}
