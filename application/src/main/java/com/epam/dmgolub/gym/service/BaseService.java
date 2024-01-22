package com.epam.dmgolub.gym.service;

import java.util.List;

public interface BaseService<K, T> {

	T findById(K id);

	List<T> findAll();
}
