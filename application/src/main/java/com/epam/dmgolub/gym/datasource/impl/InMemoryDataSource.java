package com.epam.dmgolub.gym.datasource.impl;

import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.datasource.IdSequence;
import com.epam.dmgolub.gym.entity.BaseEntity;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component
public class InMemoryDataSource implements DataSource<Long> {

	private static final String VALUE_DELIMITER = ";";

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDataSource.class);

	@Value("${trainee.data.filePath}")
	private String traineeDataFilePath;
	@Value("${trainer.data.filePath}")
	private String trainerDataFilePath;
	@Value("${training.data.filePath}")
	private String trainingDataFilePath;
	@Value("${training-type.data.filePath}")
	private String trainingTypeDataFilePath;

	private final Map<Class<?>, Map<Long, BaseEntity<Long>>> data;
	private final Map<Class<?>, IdSequence<Long>> idSequencesToClasses;

	public InMemoryDataSource() {
		this.data = new HashMap<>();
		data.put(Trainee.class, new HashMap<>());
		data.put(Trainer.class, new HashMap<>());
		data.put(Training.class, new HashMap<>());
		data.put(TrainingType.class, new HashMap<>());

		this.idSequencesToClasses = new HashMap<>();
		idSequencesToClasses.put(User.class, getNewIdSequence());
		idSequencesToClasses.put(Trainee.class, getNewIdSequence());
		idSequencesToClasses.put(Trainer.class, getNewIdSequence());
		idSequencesToClasses.put(Training.class, getNewIdSequence());
		idSequencesToClasses.put(TrainingType.class, getNewIdSequence());
	}

	@PostConstruct
	void init() {
		initializeTraineeData();
		initializeTrainingTypeData();
		initializeTrainerData();
		initializeTrainingData();
	}

	@Override
	public Map<Class<?>, Map<Long, BaseEntity<Long>>> getData() {
		return data;
	}

	@Override
	public Map<Class<?>, IdSequence<Long>> getIdSequencesToClasses() {
		return idSequencesToClasses;
	}

	private void initializeTraineeData() {
		final List<String> lines = readLinesFromFile(traineeDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final Trainee trainee = new Trainee();
			trainee.setId(idSequencesToClasses.get(Trainee.class).generateNewId());
			trainee.setUserId(idSequencesToClasses.get(User.class).generateNewId());
			setUserProperties(trainee, values);
			trainee.setDateOfBirth(parseDate(values[5]));
			trainee.setAddress(values[6]);
			data.get(Trainee.class).put(trainee.getId(), trainee);
		}
	}

	private void initializeTrainingTypeData() {
		final List<String> lines = readLinesFromFile(trainingTypeDataFilePath);
		for (String typeName : lines) {
			final Long id = idSequencesToClasses.get(TrainingType.class).generateNewId();
			data.get(TrainingType.class).put(id, new TrainingType(id, typeName));
		}
	}

	private void initializeTrainerData() {
		final List<String> lines = readLinesFromFile(trainerDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final Trainer trainer = new Trainer();
			trainer.setId(idSequencesToClasses.get(Trainer.class).generateNewId());
			trainer.setUserId(idSequencesToClasses.get(User.class).generateNewId());
			setUserProperties(trainer, values);
			trainer.setSpecialization((TrainingType) data.get(TrainingType.class).get(Long.valueOf(values[5])));
			data.get(Trainer.class).put(trainer.getId(), trainer);
		}
	}

	private void initializeTrainingData() {
		final List<String> lines = readLinesFromFile(trainingDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final Long id = idSequencesToClasses.get(Training.class).generateNewId();
			final var trainee = (Trainee) data.get(Trainee.class).get(Long.valueOf(values[0]));
			final var trainer = (Trainer) data.get(Trainer.class).get(Long.valueOf(values[1]));
			final String name = values[2];
			final var type = (TrainingType) data.get(TrainingType.class).get(Long.valueOf(values[3]));
			final Date date = parseDate(values[4]);
			final int duration = Integer.parseInt(values[5]);
			final Training training = new Training(id, trainee, trainer, name, type, date, duration);
			data.get(Training.class).put(id, training);
		}
	}

	private List<String> readLinesFromFile(final String filePath) {
		List<String> lines = new ArrayList<>();
		try (final var reader = new BufferedReader(new InputStreamReader(
			Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)),
			StandardCharsets.UTF_8))
		) {
			lines = reader.lines().toList();
			if (lines.isEmpty()) {
				throw new IllegalStateException("File " + filePath + " must not be empty");
			}
		} catch (final IOException e) {
			LOGGER.error("Exception while trying to read from file {}: {}", filePath, e.toString());
		}
		return lines;
	}

	private IdSequence<Long> getNewIdSequence() {
		return new InMemoryIdSequence<>(1L, prev -> prev + 1);
	}

	private Date parseDate(final String date) {
		try {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			return formatter.parse(date);
		} catch (final ParseException e) {
			LOGGER.error("Exception while parsing date from string {}", date);
			return null;
		}
	}

	private void setUserProperties(final User user, final String[] values) {
		user.setFirstName(values[0]);
		user.setLastName(values[1]);
		user.setUserName(values[2]);
		user.setPassword(values[3]);
		user.setActive(Boolean.parseBoolean(values[4]));
	}
}
