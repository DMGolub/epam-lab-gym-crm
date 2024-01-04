package com.epam.dmgolub.gym.dao;

import com.epam.dmgolub.gym.entity.Training;

public interface TrainingDAO extends BaseDAO<Long, Training> {

	Training save(Training training);

	void delete(Long id);
}
