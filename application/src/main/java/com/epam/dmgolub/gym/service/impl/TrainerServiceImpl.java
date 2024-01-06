package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
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
	private final MapStructMapper mapper;
	private final UserCredentialsGenerator userCredentialsGenerator;

	public TrainerServiceImpl(
		final UserRepository userRepository,
		final TrainerRepository trainerRepository,
		final MapStructMapper mapper,
		final UserCredentialsGenerator userCredentialsGenerator
	) {
		this.userRepository = userRepository;
		this.trainerRepository = trainerRepository;
		this.mapper = mapper;
		this.userCredentialsGenerator = userCredentialsGenerator;
	}

	@Override
	public TrainerResponseDTO save(final TrainerRequestDTO request) {
		LOGGER.debug("In save - saving trainer from request {}", request);
		final var trainer = mapper.trainerRequestDTOToTrainer(request);
		trainer.getUser().setUserName(userCredentialsGenerator.generateUserName(trainer.getUser()));
		trainer.getUser().setPassword(userCredentialsGenerator.generatePassword(trainer.getUser()));
		trainer.setUser(userRepository.saveAndFlush(trainer.getUser()));
		return mapper.trainerToTrainerResponseDTO(trainerRepository.saveAndFlush(trainer));
	}

	@Override
	public TrainerResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainer by id={} from repository", id);
		final var trainer = getById(id);
		return mapper.trainerToTrainerResponseDTO(trainer);
	}

	@Override
	public List<TrainerResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainers from repository");
		return mapper.trainerListToTrainerResponseDTOList(trainerRepository.findAll());
	}

	@Override
	public TrainerResponseDTO findByUserName(final String userName) {
		LOGGER.debug("In findByUserName - Fetching trainer by userName={} from repository", userName);
		final var trainer = trainerRepository.findByUserUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_USERNAME_MESSAGE + userName));
		return mapper.trainerToTrainerResponseDTO(trainer);
	}

	@Override
	public TrainerResponseDTO update(final TrainerRequestDTO request) {
		LOGGER.debug("In update - Updating trainer from request {}", request);
		final var trainer = getById(request.getId());
		if (namesAreNotEqual(request, trainer)) {
			trainer.getUser().setFirstName(request.getFirstName());
			trainer.getUser().setLastName(request.getLastName());
			trainer.getUser().setUserName(userCredentialsGenerator.generateUserName(trainer.getUser()));
		}
		trainer.getUser().setActive(request.isActive());
		trainer.setSpecialization(mapper.trainingTypeDTOToTrainingType(request.getSpecialization()));
		return mapper.trainerToTrainerResponseDTO(trainerRepository.saveAndFlush(trainer));
	}

	private Trainer getById(final Long id) {
		return trainerRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_BY_ID_MESSAGE + id));
	}

	private boolean namesAreNotEqual(final TrainerRequestDTO request, final Trainer trainer) {
		return !request.getFirstName().equals(trainer.getUser().getFirstName()) ||
			!request.getLastName().equals(trainer.getUser().getLastName());
	}
}
