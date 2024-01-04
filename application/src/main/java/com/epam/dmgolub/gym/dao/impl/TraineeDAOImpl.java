package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.TraineeDAO;
import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TraineeDAOImpl implements TraineeDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDAOImpl.class);

	private DataSource<Long> dataSource;

	@Autowired
	public void setDataSource(final DataSource<Long> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Trainee save(final Trainee trainee) {
		LOGGER.debug("In save - Saving trainee {} in data source", trainee);
		try {
			final var idSequences = dataSource.getIdSequencesToClasses();
			trainee.setId(idSequences.get(Trainee.class).generateNewId());
			trainee.setUserId(idSequences.get(User.class).generateNewId());
			dataSource.getData().get(Trainee.class).put(trainee.getId(), trainee);
			return trainee;
		} catch (final ClassCastException | NullPointerException |
					   UnsupportedOperationException | IllegalArgumentException e) {
			throw new DAOException("Exception while trying to save trainee " + trainee, e);
		}
	}

	@Override
	public Optional<Trainee> findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainee by id={} from data source", id);
		try {
			final var trainee = (Trainee) dataSource.getData().get(Trainee.class).get(id);
			return (trainee != null) ? Optional.of(trainee) : Optional.empty();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching trainee by id=" + id, e);
		}
	}

	@Override
	public List<Trainee> findAll() {
		LOGGER.debug("In findAll - Fetching all trainees from data source");
		try {
			return  dataSource.getData().get(Trainee.class).values().stream().map(v -> (Trainee) v).toList();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching all trainees from data source", e);
		}
	}

	@Override
	public Trainee update(final Trainee trainee) {
		try {
			dataSource.getData().get(Trainee.class).put(trainee.getId(), trainee);
			return trainee;
		} catch (final ClassCastException | NullPointerException |
					   UnsupportedOperationException | IllegalArgumentException e) {
			throw new DAOException("Exception while trying to update trainee " + trainee, e);
		}
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - trying to remove trainer with id={} from data source", id);
		try {
			dataSource.getData().get(Trainee.class).remove(id);
		} catch (final ClassCastException | NullPointerException | UnsupportedOperationException e) {
			throw new DAOException("Exception while trying to delete trainee with id=" + id, e);
		}
	}
}
