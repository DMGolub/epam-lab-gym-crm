package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;

import java.util.List;

public interface TrainingService {

	TrainingModel save(TrainingModel training);

	List<TrainingModel> searchByTrainee(TraineeTrainingsSearchRequest request);

	List<TrainingModel> searchByTrainer(TrainerTrainingsSearchRequest request);

	List<TrainingModel> findAll();

	void delete(TrainingModel training);
}
