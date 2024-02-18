package com.epam.dmgolub.gym.repository.impl;

import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.TrainerWorkload;
import com.epam.dmgolub.gym.repository.WorkloadRepository;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@Repository
public class WorkloadRepositoryImpl implements WorkloadRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadRepositoryImpl.class);

	private final DataSource dataSource;

	public WorkloadRepositoryImpl(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public TrainerWorkload findByTrainerUserName(final String userName) {
		LOGGER.debug("[{}] In findByTrainerUserName - Received request to find workload by trainer userName={}",
			MDC.get(TRANSACTION_ID), userName);
		return dataSource.getData().get(userName);
	}

	@Override
	public void saveOfUpdate(final TrainerWorkload trainerWorkload) {
		LOGGER.debug("[{}] In update - Received request to update workload: {}",
			MDC.get(TRANSACTION_ID), trainerWorkload);
		dataSource.getData().put(trainerWorkload.getTrainerUserName(), trainerWorkload);
	}
}
