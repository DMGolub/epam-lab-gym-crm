package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TrainerModel;

import java.util.List;

public interface TrainerService {

	List<TrainerModel> findAll();

	TrainerModel findByUserName(String userName);

	Credentials save(TrainerModel trainer);

	TrainerModel update(TrainerModel trainer);

	List<TrainerModel> findActiveTrainersAssignedOnTrainee(String userName);

	List<TrainerModel> findActiveTrainersNotAssignedOnTrainee(String userName);
}
