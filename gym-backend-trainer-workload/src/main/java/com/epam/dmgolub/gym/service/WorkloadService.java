package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;

public interface WorkloadService {

	void addWorkload(WorkloadUpdateRequest training);

	void deleteWorkload(WorkloadUpdateRequest training);
}
