package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface TrainingService {

	void save(TrainingCreateRequestDTO training, HttpSession session);

	List<TrainingResponseDTO> searchByTrainee(TraineeTrainingsSearchRequestDTO searchRequest, HttpSession session);

	List<TrainingResponseDTO> searchByTrainer(TrainerTrainingsSearchRequestDTO searchRequest, HttpSession session);
}
