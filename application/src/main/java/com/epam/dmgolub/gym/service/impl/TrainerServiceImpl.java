package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_BY_ID_MESSAGE;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINER_NOT_FOUND_BY_USERNAME_MESSAGE;

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
	public TrainerModel save(final TrainerModel request) {
		LOGGER.debug("In save - Saving trainer from request {}", request);
		final var trainer = mapper.trainerModelToTrainer(request);
		trainer.getUser().setUserName(userCredentialsGenerator.generateUserName(trainer.getUser()));
		trainer.getUser().setPassword(userCredentialsGenerator.generatePassword(trainer.getUser()));
		trainer.setUser(userRepository.saveAndFlush(trainer.getUser()));
		return mapper.trainerToTrainerModel(trainerRepository.saveAndFlush(trainer));
	}

	@Override
	public TrainerModel findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainer by id={} from repository", id);
		final var trainer = getById(id);
		return mapper.trainerToTrainerModel(trainer);
	}

	@Override
	public List<TrainerModel> findAll() {
		LOGGER.debug("In findAll - Fetching all trainers from repository");
		return mapper.trainerListToTrainerModelList(trainerRepository.findAll());
	}

	@Override
	public TrainerModel findByUserName(final String userName) {
		LOGGER.debug("In findByUserName - Fetching trainer by userName={} from repository", userName);
		final var trainer = trainerRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_USERNAME_MESSAGE + userName));
		return mapper.trainerToTrainerModel(trainer);
	}

	@Override
	public TrainerModel update(final TrainerModel request) {
		LOGGER.debug("In update - Updating trainer from request {}", request);
		final var trainer = getById(request.getId());
		trainer.getUser().setFirstName(request.getFirstName());
		trainer.getUser().setLastName(request.getLastName());
		trainer.getUser().setActive(request.isActive());
		trainer.setSpecialization(mapper.trainingTypeModelToTrainingType(request.getSpecialization()));
		return mapper.trainerToTrainerModel(trainerRepository.saveAndFlush(trainer));
	}

	@Override
	public List<TrainerModel> findActiveTrainersAssignedToTrainee(final Long id) {
		LOGGER.debug("In findActiveTrainersAssignedToTrainee - Fetching assigned trainers for id={}", id);
		final var trainers = trainerRepository.findActiveTrainersAssignedToTrainee(id);
		return mapper.trainerListToTrainerModelList(trainers);
	}

	@Override
	public List<TrainerModel> findActiveTrainersNotAssignedToTrainee(final Long id) {
		LOGGER.debug("In findActiveTrainersNotAssignedToTrainee - Fetching not assigned trainers for id={}", id);
		final var trainers = trainerRepository.findActiveTrainersNotAssignedToTrainee(id);
		return mapper.trainerListToTrainerModelList(trainers);
	}

	private Trainer getById(final Long id) {
		return trainerRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID_MESSAGE + id));
	}
}
