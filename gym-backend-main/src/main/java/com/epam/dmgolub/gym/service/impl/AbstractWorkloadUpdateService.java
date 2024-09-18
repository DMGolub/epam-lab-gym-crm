package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.service.WorkloadUpdateService;

public abstract class AbstractWorkloadUpdateService implements WorkloadUpdateService {

	protected TrainerWorkloadUpdateRequestDTO createRequestDTO(final TrainingModel training, final String actionType) {
		return new TrainerWorkloadUpdateRequestDTO(
			training.getTrainer().getUserName(),
			training.getTrainer().getFirstName(),
			training.getTrainer().getLastName(),
			training.getTrainer().isActive(),
			training.getDate(),
			training.getDuration(),
			actionType
		);
	}
}
