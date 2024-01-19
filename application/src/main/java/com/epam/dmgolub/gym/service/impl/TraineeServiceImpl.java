package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
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
import java.util.function.Predicate;

import static com.epam.dmgolub.gym.service.constant.Constants.TRAINEE_NOT_FOUND_BY_ID_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINEE_NOT_FOUND_BY_USERNAME_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_BY_ID_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_BY_USERNAME_MESSAGE;

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
		final var trainee = mapper.mapToTrainee(request);
		trainee.getUser().setUserName(userCredentialsGenerator.generateUserName(trainee.getUser()));
		trainee.getUser().setPassword(userCredentialsGenerator.generatePassword(trainee.getUser()));
		trainee.setUser(userRepository.saveAndFlush(trainee.getUser()));
		return mapper.mapToTraineeModel(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public TraineeModel findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainee by id={} from repository", id);
		final var trainee = getTrainee(id);
		return mapper.mapToTraineeModel(trainee);
	}

	@Override
	public List<TraineeModel> findAll() {
		LOGGER.debug("In findAll - Fetching all trainees from repository");
		return mapper.mapToTraineeModelList(traineeRepository.findAll());
	}

	@Override
	public TraineeModel findByUserName(final String userName) {
		LOGGER.debug("In findByUserName - Fetching trainee by userName={} from repository", userName);
		final var trainee = getTrainee(userName);
		return mapper.mapToTraineeModel(trainee);
	}

	@Override
	public TraineeModel update(final TraineeModel request) {
		LOGGER.debug("In update - Updating trainee from request {}", request);
		final var trainee = getTrainee(request.getUserName());
		trainee.getUser().setFirstName(request.getFirstName());
		trainee.getUser().setLastName(request.getLastName());
		trainee.getUser().setActive(request.isActive());
		trainee.setDateOfBirth(request.getDateOfBirth());
		trainee.setAddress(request.getAddress());
		return mapper.mapToTraineeModel(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - Fetching trainings before removing trainee by id={}", id);
		getTrainee(id);
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
		getTrainee(userName);
		removeTrainings(userName);
		traineeRepository.deleteByUserUserName(userName);
	}

	@Override
	public void addTrainer(final Long traineeId, final Long trainerId) {
		LOGGER.debug("In addTrainer - Fetching trainer by id={} from repository", trainerId);
		final var trainer = trainerRepository.findById(trainerId)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID_MESSAGE + trainerId));
		LOGGER.debug("In addTrainer - Fetching trainee by id={} from repository and adding trainer", traineeId);
		getTrainee(traineeId).getTrainers().add(trainer);
	}

	@Override
	public void updateTrainers(final String traineeUserName, final List<String> trainerUserNames) {
		LOGGER.debug("In updateTrainers - Fetching trainee by userName={} from repository", traineeUserName);
		final var trainee = getTrainee(traineeUserName);
		LOGGER.debug("In updateTrainers - Determining trainers to remove and add");
		final var trainersToAdd = determineTrainersToAdd(trainee.getTrainers(), trainerUserNames);
		final var trainersToRemove = determineTrainersToRemove(trainee.getTrainers(), trainerUserNames);
		trainersToAdd.forEach(trainer -> trainee.getTrainers().add(trainer));
		trainersToRemove.forEach(trainer -> removeTrainings(traineeUserName, trainer.getUser().getUserName()));
		trainee.getTrainers().removeAll(trainersToRemove);
		LOGGER.debug("In updateTrainers - Removed {} trainers, assigned {} trainers to trainee={}",
			trainersToRemove.size(), trainersToAdd.size(), traineeUserName);
		traineeRepository.saveAndFlush(trainee);
	}

	private List<Trainer> determineTrainersToAdd(final List<Trainer> trainers, final List<String> trainerUserNames) {
		return trainerUserNames.stream()
			.filter(userName -> trainers.stream().noneMatch(t -> t.getUser().getUserName().equals(userName)))
			.map(this::getTrainer).toList();
	}

	private List<Trainer> determineTrainersToRemove(final List<Trainer> trainers, final List<String> trainerUserNames) {
		return trainers.stream()
			.filter(trainer -> !trainerUserNames.contains(trainer.getUser().getUserName()))
			.toList();
	}

	private Trainee getTrainee(final Long id) {
		return traineeRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_ID_MESSAGE + id));
	}

	private Trainee getTrainee(final String userName) {
		return traineeRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_BY_USERNAME_MESSAGE + userName));
	}

	private Trainer getTrainer(final String userName) {
		return trainerRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_USERNAME_MESSAGE + userName));
	}

	private void removeTrainings(final String traineeUserName) {
		final List<Training> trainings = trainingRepository.findAll().stream()
			.filter(t -> traineeUserName.equals(t.getTrainee().getUser().getUserName()))
			.toList();
		trainingRepository.deleteAll(trainings);
		LOGGER.debug("In removeTrainings - Removed {} trainings associated to trainee={}",
			trainings.size(), traineeUserName);
	}

	private void removeTrainings(final String traineeUserName, final String trainerUserName) {
		final Predicate<Training> isAssociatedToTraineeAndTrainer = training ->
			traineeUserName.equals(training.getTrainee().getUser().getUserName()) &&
			trainerUserName.equals(training.getTrainer().getUser().getUserName());
		final List<Training> trainings = trainingRepository.findAll().stream()
			.filter(isAssociatedToTraineeAndTrainer)
			.toList();
		trainingRepository.deleteAll(trainings);
		LOGGER.debug("In removeTrainings - Removed {} trainings associated to trainee={} and trainer={}",
			trainings.size(), traineeUserName, trainerUserName);
	}
}
