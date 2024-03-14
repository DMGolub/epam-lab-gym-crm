package com.epam.dmgolub.gym.datasource.impl;

import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.TrainerWorkload;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryDataSource implements DataSource {

	private final Map<String, TrainerWorkload> data;

	public InMemoryDataSource() {
		this.data = new HashMap<>();
	}

	@Override
	public Map<String, TrainerWorkload> getData() {
		return data;
	}
}
