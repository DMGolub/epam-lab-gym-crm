package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;

import java.util.List;

public interface TrainingService extends BaseService<Long, TrainingRequestDTO, TrainingResponseDTO> {

	TrainingResponseDTO save(TrainingRequestDTO training);

	List<TrainingResponseDTO> searchByTrainee(TraineeTrainingsSearchRequestDTO request);

	List<TrainingResponseDTO> searchByTrainer(TrainerTrainingsSearchRequestDTO request);
}
