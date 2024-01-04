package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.TrainingTypeDAO;
import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainingTypeDAOImpl implements TrainingTypeDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeDAOImpl.class);

	private DataSource<Long> dataSource;

	@Autowired
	public void setDataSource(final DataSource<Long> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Optional<TrainingType> findById(final Long id) {
		LOGGER.debug("In findById - Fetching training type by id={} from data source", id);
		try {
			final var trainingType = (TrainingType) dataSource.getData().get(TrainingType.class).get(id);
			return (trainingType != null) ? Optional.of(trainingType) : Optional.empty();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching training type by id=" + id, e);
		}
	}

	@Override
	public List<TrainingType> findAll() {
		LOGGER.debug("In findAll - Fetching all training types from data source");
		try {
			return dataSource.getData().get(TrainingType.class).values().stream()
				.map(v -> (TrainingType) v)
				.toList();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching all training types from data source", e);
		}
	}
}
