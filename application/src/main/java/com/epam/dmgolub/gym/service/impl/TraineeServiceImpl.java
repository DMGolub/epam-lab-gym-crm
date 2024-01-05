package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.repository.TraineeRepository;
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

@Service
@Transactional
public class TraineeServiceImpl implements TraineeService {

	private static final String TRAINEE_NOT_FOUND_MESSAGE = "Can not find trainee by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

	private final UserRepository userRepository;
	private final TraineeRepository traineeRepository;
	private final TrainingRepository trainingRepository;
	private final MapStructMapper mapper;
	private final UserCredentialsGenerator userCredentialsGenerator;

	public TraineeServiceImpl(
		final UserRepository userRepository,
		final TraineeRepository traineeRepository,
		final TrainingRepository trainingRepository,
		final MapStructMapper mapper,
		final UserCredentialsGenerator userCredentialsGenerator
	) {
		this.userRepository = userRepository;
		this.traineeRepository = traineeRepository;
		this.trainingRepository = trainingRepository;
		this.mapper = mapper;
		this.userCredentialsGenerator = userCredentialsGenerator;
	}

	@Override
	public TraineeResponseDTO save(final TraineeRequestDTO request) {
		LOGGER.debug("In save - saving trainee from request {}", request);
		final var trainee = mapper.traineeRequestDTOToTrainee(request);
		trainee.getUser().setUserName(userCredentialsGenerator.generateUserName(trainee.getUser()));
		trainee.getUser().setPassword(userCredentialsGenerator.generatePassword(trainee.getUser()));
		trainee.setUser(userRepository.saveAndFlush(trainee.getUser()));
		return mapper.traineeToTraineeResponseDTO(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public TraineeResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainee by id={} from repository", id);
		final var trainee = getById(id);
		return mapper.traineeToTraineeResponseDTO(trainee);
	}

	@Override
	public List<TraineeResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainees from repository");
		return mapper.traineeListToTraineeResponseDTOList(traineeRepository.findAll());
	}

	@Override
	public TraineeResponseDTO update(final TraineeRequestDTO request) {
		LOGGER.debug("In update - Updating trainee from request {}", request);
		final var trainee = getById(request.getId());
		if (namesAreNotEqual(request, trainee)) {
			trainee.getUser().setFirstName(request.getFirstName());
			trainee.getUser().setLastName(request.getLastName());
			trainee.getUser().setUserName(userCredentialsGenerator.generateUserName(trainee.getUser()));
		}
		trainee.getUser().setActive(request.isActive());
		trainee.setDateOfBirth(request.getDateOfBirth());
		trainee.setAddress(request.getAddress());
		return mapper.traineeToTraineeResponseDTO(traineeRepository.saveAndFlush(trainee));
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - fetching trainings before removing trainee by id={}", id);
		final List<Training> trainings = trainingRepository.findAll().stream()
			.filter(t -> id.equals(t.getTrainee().getId()))
			.toList();
		trainingRepository.deleteAll(trainings);
		LOGGER.debug("In delete - removed {} trainings, removing trainee", trainings.size());
		traineeRepository.deleteById(id);
	}

	private Trainee getById(final Long id) {
		return traineeRepository
			.findById(id).orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_MESSAGE + id));
	}

	private boolean namesAreNotEqual(final TraineeRequestDTO request, final Trainee trainee) {
		return !request.getFirstName().equals(trainee.getUser().getFirstName()) ||
			!request.getLastName().equals(trainee.getUser().getLastName());
	}
}
