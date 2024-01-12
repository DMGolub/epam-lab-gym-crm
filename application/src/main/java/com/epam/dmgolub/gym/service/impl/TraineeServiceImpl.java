package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.TRAINEE_NOT_FOUND_BY_ID_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINEE_NOT_FOUND_BY_USERNAME_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_BY_ID_MESSAGE;

@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

	private final UserRepository userRepository;
	private final TraineeRepository traineeRepository;
	private final TrainingRepository trainingRepository;
	private final TrainerRepository trainerRepository;
	private final EntityToModelMapper mapper;
	private final UserCredentialsGenerator userCredentialsGenerator;

	public TraineeServiceImpl(
		final UserRepository userRepository,
		final TraineeRepository traineeRepository,
		final TrainingRepository trainingRepository,
		final TrainerRepository trainerRepository,
		final EntityToModelMapper mapper,
		final UserCredentialsGenerator userCredentialsGenerator
	) {
		this.userRepository = userRepository;
		this.traineeRepository = traineeRepository;
		this.trainingRepository = trainingRepository;
		this.trainerRepository = trainerRepository;
		this.mapper = mapper;
		this.userCredentialsGenerator = userCredentialsGenerator;
	}

	@Override
	public TraineeModel save(final TraineeModel request) {
		LOGGER.debug("In save - Saving trainee from request {}", request);
		final var trainee = mapper.traineeModelToTrainee(request);
		trainee.getUser().setUserName(userCredentialsGenerator.generateUserName(trainee.getUser()));
		trainee.getUser().setPassword(userCredentialsGenerator.generatePassword(trainee.getUser()));
		trainee.setUser(userRepository.saveAndFlush(trainee.getUser()));
		return mapper.traineeToTraineeModel(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public TraineeModel findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainee by id={} from repository", id);
		final var trainee = getById(id);
		return mapper.traineeToTraineeModel(trainee);
	}

	@Override
	public List<TraineeModel> findAll() {
		LOGGER.debug("In findAll - Fetching all trainees from repository");
		return mapper.traineeListToTraineeModelList(traineeRepository.findAll());
	}

	@Override
	public TraineeModel findByUserName(final String userName) {
		LOGGER.debug("In userName - Fetching trainee by userName={} from repository", userName);
		final var trainee = getByUserName(userName);
		return mapper.traineeToTraineeModel(trainee);
	}

	@Override
	public TraineeModel update(final TraineeModel request) {
		LOGGER.debug("In update - Updating trainee from request {}", request);
		final var trainee = getById(request.getId());
		trainee.getUser().setFirstName(request.getFirstName());
		trainee.getUser().setLastName(request.getLastName());
		trainee.getUser().setActive(request.isActive());
		trainee.setDateOfBirth(request.getDateOfBirth());
		trainee.setAddress(request.getAddress());
		return mapper.traineeToTraineeModel(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - Fetching trainings before removing trainee by id={}", id);
		final List<Training> trainings = trainingRepository.findAll().stream()
			.filter(t -> id.equals(t.getTrainee().getId()))
			.toList();
		trainingRepository.deleteAll(trainings);
		LOGGER.debug("In delete - Removed {} trainings, removing trainee by id", trainings.size());
		traineeRepository.deleteById(id);
	}

	@Override
	public void delete(final String userName) {
		LOGGER.debug("In delete - Fetching trainings before removing trainee by id={}", userName);
		final List<Training> trainings = trainingRepository.findAll().stream()
			.filter(t -> userName.equals(t.getTrainee().getUser().getUserName()))
			.toList();
		trainingRepository.deleteAll(trainings);
		LOGGER.debug("In delete - Removed {} trainings, removing trainee by userName", trainings.size());
		traineeRepository.deleteByUserUserName(userName);
	}

	@Override
	public void addTrainer(final Long traineeId, final Long trainerId) {
		LOGGER.debug("In addTrainer - Fetching trainer by id={} from repository", trainerId);
		final var trainer = trainerRepository.findById(trainerId)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID_MESSAGE + trainerId));
		LOGGER.debug("In addTrainer - Fetching trainee by id={} from repository and adding trainer", traineeId);
		getById(traineeId).getTrainers().add(trainer);
	}

	private Trainee getById(final Long id) {
		return traineeRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID_MESSAGE + id));
	}

	private Trainee getByUserName(final String userName) {
		return traineeRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_USERNAME_MESSAGE + userName));
	}
}
