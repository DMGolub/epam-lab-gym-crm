package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.dto.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {

	List<TrainingTypeDTO> findAll();

	TrainingTypeDTO findById(Long id);

	TrainingTypeDTO findByLink(String link);
}
