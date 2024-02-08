package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TraineeModel;

import java.util.List;

public interface TraineeService {

	List<TraineeModel> findAll();

	TraineeModel findByUserName(String userName);

	Credentials save(TraineeModel request);

	TraineeModel update(TraineeModel trainee);

	void delete(String userName);

	void updateTrainers(String traineeUserName, List<String> trainerUserNames);
}
