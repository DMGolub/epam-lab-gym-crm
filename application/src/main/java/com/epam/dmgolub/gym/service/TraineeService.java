package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;

public interface TraineeService extends BaseService<Long, TraineeRequestDTO, TraineeResponseDTO> {

	TraineeResponseDTO findByUserName(String userName);

	TraineeResponseDTO save(TraineeRequestDTO request);

	TraineeResponseDTO update(TraineeRequestDTO trainee);

	void delete(Long id);

	void delete(String userName);
}
