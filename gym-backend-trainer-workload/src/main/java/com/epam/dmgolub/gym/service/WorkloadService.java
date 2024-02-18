package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;

public interface WorkloadService {

	void addTraining(WorkloadUpdateRequest training);

	boolean deleteTraining(WorkloadUpdateRequest training);
}
