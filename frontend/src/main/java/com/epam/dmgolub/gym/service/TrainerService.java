package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface TrainerService {

	SignUpResponseDTO save(TrainerRequestDTO trainer);

	TrainerResponseDTO findByUserName(String userName, HttpSession session);

	List<TrainerResponseDTO> findAll(HttpSession session);

	void update(TrainerRequestDTO trainer, HttpSession session);

	List<TraineeResponseDTO.TrainerDTO> findActiveTrainersNotAssignedOnTrainee(String userName, HttpSession session);

	List<TraineeResponseDTO.TrainerDTO> findActiveTrainersAssignedOnTrainee(String userName, HttpSession session);
}
