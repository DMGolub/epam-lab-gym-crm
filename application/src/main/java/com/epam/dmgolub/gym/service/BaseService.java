package com.epam.dmgolub.gym.service;

import java.util.List;

public interface BaseService<K, Req, Resp> {

	Resp findById(K id);
	List<Resp> findAll();
}
