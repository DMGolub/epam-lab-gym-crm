package com.epam.dmgolub.gym.service;

import com.epam.dmgolub.gym.model.TrainingTypeModel;

import java.util.List;

public interface TrainingTypeService {

	List<TrainingTypeModel> findAll();

	TrainingTypeModel findById(Long id);
}
