package com.epam.dmgolub.gym.dao;

import com.epam.dmgolub.gym.entity.Trainee;

public interface TraineeDAO extends BaseDAO<Long, Trainee> {

	Trainee save(Trainee trainee);
	Trainee update(Trainee trainee);
	void delete(Long id);
}
