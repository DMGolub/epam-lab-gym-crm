package com.epam.dmgolub.gym.datasource;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
@ConditionalOnProperty(name = "dummy.data.initialization", havingValue = "true")
public class DummyDataInitializer {

	private static final String VALUE_DELIMITER = ";";

	private static final Logger LOGGER = LoggerFactory.getLogger(DummyDataInitializer.class);

	@NotBlank
	@Value("${dummy.data.trainee.filePath}")
	private String traineeDataFilePath;
	@NotBlank
	@Value("${dummy.data.trainer.filePath}")
	private String trainerDataFilePath;
	@NotBlank
	@Value("${dummy.data.trainersToTrainees.filePath}")
	private String trainersToTraineesFilePath;
	@NotBlank
	@Value("${dummy.data.training.filePath}")
	private String trainingDataFilePath;
	@NotBlank
	@Value("${dummy.data.training-type.filePath}")
	private String trainingTypeDataFilePath;

	private final UserRepository userRepository;
	private final TrainingTypeRepository trainingTypeRepository;
	private final TrainerRepository trainerRepository;
	private final TraineeRepository traineeRepository;
	private final TrainingRepository trainingRepository;
	private final PasswordEncoder passwordEncoder;

	public DummyDataInitializer(
		final UserRepository userRepository,
		final TrainingTypeRepository trainingTypeRepository,
		final TrainerRepository trainerRepository,
		final TraineeRepository traineeRepository,
		final TrainingRepository trainingRepository,
		final PasswordEncoder passwordEncoder
	) {
		this.userRepository = userRepository;
		this.trainingTypeRepository = trainingTypeRepository;
		this.trainerRepository = trainerRepository;
		this.traineeRepository = traineeRepository;
		this.trainingRepository = trainingRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	void init() {
		initializeTrainingTypeData();
		initializeTrainerData();
		initializeTraineeData();
		initializeTrainingData();
		initializeAdminData();
	}

	private void initializeTrainingTypeData() {
		final List<String> typeNames = readLinesFromFile(trainingTypeDataFilePath);
		for (String typeName : typeNames) {
			trainingTypeRepository.saveAndFlush(new TrainingType(null, typeName));
		}
	}

	private void initializeTrainerData() {
		final List<String> lines = readLinesFromFile(trainerDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final Trainer trainer = new Trainer();
			setUserProperties(trainer.getUser(), values);
			trainer.setSpecialization(trainingTypeRepository.findById(Long.valueOf(values[5])).get());
			trainer.setUser(userRepository.saveAndFlush(trainer.getUser()));
			trainerRepository.saveAndFlush(trainer);
		}
	}

	private void initializeTraineeData() {
		final List<String> lines = readLinesFromFile(traineeDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final Trainee trainee = new Trainee();
			setUserProperties(trainee.getUser(), values);
			trainee.setDateOfBirth(parseDate(values[5]));
			trainee.setAddress(values[6]);
			trainee.setUser(userRepository.saveAndFlush(trainee.getUser()));
			traineeRepository.saveAndFlush(trainee);
		}
		addTrainersToTrainees();
	}

	private void addTrainersToTrainees() {
		final List<String> lines = readLinesFromFile(trainersToTraineesFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final long traineeId = Long.parseLong(values[0]);
			final long trainerId = Long.parseLong(values[1]);
			final Trainee trainee = traineeRepository.findById(traineeId).get();
			final Trainer trainer = trainerRepository.findById(trainerId).get();
			trainee.getTrainers().add(trainer);
			traineeRepository.saveAndFlush(trainee);
		}
	}

	private void initializeTrainingData() {
		final List<String> lines = readLinesFromFile(trainingDataFilePath);
		for (String line : lines) {
			final String[] values = line.split(VALUE_DELIMITER);
			final var trainee = traineeRepository.findById(Long.valueOf(values[0])).get();
			final var trainer = trainerRepository.findById(Long.valueOf(values[1])).get();
			final String name = values[2];
			final var type = trainingTypeRepository.findById(Long.valueOf(values[3])).get();
			final Date date = parseDate(values[4]);
			final int duration = Integer.parseInt(values[5]);
			final Training training = new Training(null, trainee, trainer, name, type, date, duration);
			trainingRepository.saveAndFlush(training);
		}
	}

	private void initializeAdminData() {
		final String password = passwordEncoder.encode("Password12");
		final User admin = new User(null, "admin", "admin", "admin.admin", password, true);
		userRepository.saveAndFlush(admin);
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
		user.setPassword(passwordEncoder.encode(values[3]));
		user.setActive(Boolean.parseBoolean(values[4]));
	}
}
