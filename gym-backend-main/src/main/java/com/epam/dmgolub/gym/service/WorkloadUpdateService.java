package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.TrainingModel;

public interface WorkloadUpdateService {

	void add(final TrainingModel training);

	void delete(final TrainingModel training);
}
