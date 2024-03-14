package com.epam.dmgolub.gym.datasource;

import com.epam.dmgolub.gym.entity.TrainerWorkload;

import java.util.Map;

public interface DataSource {

	Map<String, TrainerWorkload> getData();
}
