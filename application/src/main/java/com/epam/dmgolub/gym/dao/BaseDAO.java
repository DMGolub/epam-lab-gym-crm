package com.epam.dmgolub.gym.dao;

import com.epam.dmgolub.gym.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<K, T extends BaseEntity<K>> {

	Optional<T> findById(K id);
	List<T> findAll();
}
