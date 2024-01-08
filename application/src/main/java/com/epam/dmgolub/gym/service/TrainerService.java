package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;

import java.util.List;

public interface TrainerService extends BaseService<Long, TrainerRequestDTO, TrainerResponseDTO> {

	TrainerResponseDTO findByUserName(String userName);

	TrainerResponseDTO save(TrainerRequestDTO trainer);

	TrainerResponseDTO update(TrainerRequestDTO trainer);

	List<TrainerResponseDTO> findActiveTrainersNotAssignedToTrainee(Long traineeId);
}
