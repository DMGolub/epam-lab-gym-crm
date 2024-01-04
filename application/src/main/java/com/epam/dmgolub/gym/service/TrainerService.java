package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;

public interface TrainerService extends BaseService<Long, TrainerRequestDTO, TrainerResponseDTO> {

	TrainerResponseDTO save(TrainerRequestDTO trainer);
	TrainerResponseDTO update(TrainerRequestDTO trainer);
}
