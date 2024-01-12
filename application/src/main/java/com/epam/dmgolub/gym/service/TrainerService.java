package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.TrainerModel;

import java.util.List;

public interface TrainerService extends BaseService<Long, TrainerModel> {

	TrainerModel findByUserName(String userName);

	TrainerModel save(TrainerModel trainer);

	TrainerModel update(TrainerModel trainer);

	List<TrainerModel> findActiveTrainersAssignedToTrainee(Long id);

	List<TrainerModel> findActiveTrainersNotAssignedToTrainee(Long id);
}
