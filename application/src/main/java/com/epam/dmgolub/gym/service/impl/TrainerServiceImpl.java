package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_MESSAGE;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);

	private final UserRepository userRepository;
	private final TrainerRepository trainerRepository;
	private final EntityToModelMapper mapper;
	private final UserCredentialsGenerator userCredentialsGenerator;

	public TrainerServiceImpl(
		final UserRepository userRepository,
		final TrainerRepository trainerRepository,
		final EntityToModelMapper mapper,
		final UserCredentialsGenerator userCredentialsGenerator
	) {
		this.userRepository = userRepository;
		this.trainerRepository = trainerRepository;
		this.mapper = mapper;
		this.userCredentialsGenerator = userCredentialsGenerator;
	}

	@Override
	public Credentials save(final TrainerModel request) {
		LOGGER.debug("In save - Saving trainer from request {}", request);
		final var trainer = mapper.mapToTrainer(request);
		final var userName = userCredentialsGenerator.generateUserName(trainer.getUser());
		final var password = userCredentialsGenerator.generatePassword(trainer.getUser());
		trainer.getUser().setUserName(userName);
		trainer.getUser().setPassword(userCredentialsGenerator.encodePassword(password));
		trainer.setUser(userRepository.saveAndFlush(trainer.getUser()));
		trainerRepository.saveAndFlush(trainer);
		return new Credentials(userName, password);
	}

	@Override
	public List<TrainerModel> findAll() {
		LOGGER.debug("In findAll - Fetching all trainers from repository");
		return mapper.mapToTrainerModelList(trainerRepository.findAll());
	}

	@Override
	public TrainerModel findByUserName(final String userName) {
		LOGGER.debug("In findByUserName - Fetching trainer by userName={} from repository", userName);
		final var trainer = getTrainer(userName);
		return mapper.mapToTrainerModel(trainer);
	}

	@Override
	public TrainerModel update(final TrainerModel request) {
		LOGGER.debug("In update - Updating trainer from request {}", request);
		final var trainer = getTrainer(request.getUserName());
		trainer.getUser().setFirstName(request.getFirstName());
		trainer.getUser().setLastName(request.getLastName());
		trainer.getUser().setActive(request.isActive());
		trainer.setSpecialization(mapper.mapToTrainingType(request.getSpecialization()));
		return mapper.mapToTrainerModel(trainerRepository.saveAndFlush(trainer));
	}

	@Override
	public List<TrainerModel> findActiveTrainersAssignedOnTrainee(final String userName) {
		LOGGER.debug("In findActiveTrainersAssignedToTrainee - Fetching assigned trainers for trainee={}", userName);
		final var trainers = trainerRepository.findActiveTrainersAssignedOnTrainee(userName);
		return mapper.mapToTrainerModelList(trainers);
	}

	@Override
	public List<TrainerModel> findActiveTrainersNotAssignedOnTrainee(final String userName) {
		LOGGER.debug("In findActiveTrainersNotAssignedToTrainee - Fetching not assigned trainers for trainee={}", userName);
		final var trainers = trainerRepository.findActiveTrainersNotAssignedOnTrainee(userName);
		return mapper.mapToTrainerModelList(trainers);
	}

	private Trainer getTrainer(final String userName) {
		return trainerRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_MESSAGE + userName));
	}
}
