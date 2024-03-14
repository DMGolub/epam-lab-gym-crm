package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.TrainerWorkload;

public interface WorkloadRepository {

	TrainerWorkload findByTrainerUserName(String userName);
	void saveOfUpdate(TrainerWorkload trainerWorkload);
}
