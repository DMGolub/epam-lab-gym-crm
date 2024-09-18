package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface TraineeService {

	SignUpResponseDTO save(TraineeRequestDTO trainee);

	TraineeResponseDTO findByUserName(String userName, HttpSession session);

	List<TraineeResponseDTO> findAll(HttpSession session);

	void delete(String userName, HttpSession session);

	void update(TraineeRequestDTO trainee, HttpSession session);

	void addTrainer(String traineeUserName, String trainerUserName, HttpSession session);
}
