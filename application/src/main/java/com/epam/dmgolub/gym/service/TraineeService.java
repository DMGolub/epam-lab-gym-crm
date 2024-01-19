package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.TraineeModel;

import java.util.List;

public interface TraineeService extends BaseService<Long, TraineeModel> {

	TraineeModel findByUserName(String userName);

	TraineeModel save(TraineeModel request);

	TraineeModel update(TraineeModel trainee);

	void delete(Long id);

	void delete(String userName);

	void addTrainer(Long traineeId, Long trainerIds);

	void updateTrainers(String traineeUserName, List<String> trainerUserNames);
}
