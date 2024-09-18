package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.TrainerWorkload;
import org.springframework.data.repository.CrudRepository;

public interface WorkloadRepository extends CrudRepository<TrainerWorkload, String> {
	// Empty
}
