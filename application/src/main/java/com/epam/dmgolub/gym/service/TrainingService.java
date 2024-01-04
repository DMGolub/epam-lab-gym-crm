package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;

public interface TrainingService extends BaseService<Long, TrainingRequestDTO, TrainingResponseDTO> {

	TrainingResponseDTO save(TrainingRequestDTO training);
}
