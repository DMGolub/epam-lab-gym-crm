package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.TrainingDAO;
import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainingDAOImpl implements TrainingDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDAOImpl.class);

	private DataSource<Long> dataSource;

	@Autowired
	public void setDataSource(final DataSource<Long> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Training save(final Training training) {
		LOGGER.debug("In save - Saving training {} in data source", training);
		try {
			training.setId(dataSource.getIdSequencesToClasses().get(Training.class).generateNewId());
			dataSource.getData().get(Training.class).put(training.getId(), training);
			return training;
		} catch (final ClassCastException | NullPointerException |
					   UnsupportedOperationException | IllegalArgumentException e) {
			throw new DAOException("Exception while trying to save training " + training, e);
		}
	}

	@Override
	public Optional<Training> findById(final Long id) {
		LOGGER.debug("In findById - Fetching training by id={} from data source", id);
		try {
			final var training = (Training) dataSource.getData().get(Training.class).get(id);
			return (training != null) ? Optional.of(training) : Optional.empty();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching training by id=" + id, e);
		}
	}

	@Override
	public List<Training> findAll() {
		LOGGER.debug("In findAll - Fetching all trainings from data source");
		try {
			return dataSource.getData().get(Training.class).values().stream().map(v -> (Training) v).toList();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching all trainings from data source", e);
		}
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - trying to remove training with id={} from data source", id);
		try {
			dataSource.getData().get(Training.class).remove(id);
		} catch (final ClassCastException | NullPointerException | UnsupportedOperationException e) {
			throw new DAOException("Exception while trying to delete training with id=" + id, e);
		}
	}
}
