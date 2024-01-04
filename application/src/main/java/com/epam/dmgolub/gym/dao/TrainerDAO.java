package com.epam.dmgolub.gym.dao;

import com.epam.dmgolub.gym.entity.Trainer;

public interface TrainerDAO extends BaseDAO<Long, Trainer> {

	Trainer save(Trainer trainer);
	Trainer update(Trainer trainer);
}
