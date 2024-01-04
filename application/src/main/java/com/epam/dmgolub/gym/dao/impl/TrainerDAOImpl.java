package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.TrainerDAO;
import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TrainerDAOImpl implements TrainerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerDAOImpl.class);

	private DataSource<Long> dataSource;

	@Autowired
	public void setDataSource(final DataSource<Long> dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Trainer save(final Trainer trainer) {
		LOGGER.debug("In save - Saving trainer {} in data source", trainer);
		try {
			final var idSequences = dataSource.getIdSequencesToClasses();
			trainer.setId(idSequences.get(Trainer.class).generateNewId());
			trainer.setUserId(idSequences.get(User.class).generateNewId());
			dataSource.getData().get(Trainer.class).put(trainer.getId(), trainer);
			return trainer;
		} catch (final ClassCastException | NullPointerException |
					   UnsupportedOperationException | IllegalArgumentException e) {
			throw new DAOException("Exception while trying to save trainer " + trainer, e);
		}
	}

	@Override
	public Optional<Trainer> findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainer by id={} from data source", id);
		try {
			final var trainer = (Trainer) dataSource.getData().get(Trainer.class).get(id);
			return (trainer != null) ? Optional.of(trainer) : Optional.empty();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching trainer by id={}" + id, e);
		}
	}

	@Override
	public List<Trainer> findAll() {
		LOGGER.debug("In findAll - Fetching all trainers from data source");
		try {
			return dataSource.getData().get(Trainer.class).values().stream()
				.map(v -> ((Trainer) v))
				.toList();
		} catch (final ClassCastException | NullPointerException e) {
			throw new DAOException("Exception while fetching all trainers from data source {}", e);
		}
	}

	@Override
	public Trainer update(final Trainer trainer) {
		LOGGER.debug("In update - Updating trainer {} in data source", trainer);
		try {
			dataSource.getData().get(Trainer.class).put(trainer.getId(), trainer);
			return trainer;
		} catch (final ClassCastException | NullPointerException |
					   UnsupportedOperationException | IllegalArgumentException e) {
			throw new DAOException("Exception while trying to update trainer " + trainer, e);
		}
	}
}
