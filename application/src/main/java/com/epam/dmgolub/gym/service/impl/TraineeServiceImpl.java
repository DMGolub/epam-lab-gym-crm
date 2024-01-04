package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TraineeDAO;
import com.epam.dmgolub.gym.dao.TrainingDAO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {

	private static final String TRAINEE_NOT_FOUND_MESSAGE = "Can not find trainee by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

	@Autowired
	private TraineeDAO traineeDAO;
	@Autowired
	private TrainingDAO trainingDAO;
	private MapStructMapper mapper;
	private UserCredentialsGenerator userCredentialsGenerator;

	@Autowired
	public void setMapper(final MapStructMapper mapper) {
		this.mapper = mapper;
	}

	@Autowired
	public void setUserCredentialsGenerator(final UserCredentialsGenerator userCredentialsGenerator) {
		this.userCredentialsGenerator = userCredentialsGenerator;
	}

	@Override
	public TraineeResponseDTO save(final TraineeRequestDTO request) {
		LOGGER.debug("In save - saving trainee from request {}", request);
		final var trainee = mapper.traineeRequestDTOToTrainee(request);
		trainee.setUserName(userCredentialsGenerator.generateUserName(trainee));
		trainee.setPassword(userCredentialsGenerator.generatePassword(trainee));
		return mapper.traineeToTraineeResponseDTO(traineeDAO.save(trainee));
	}

	@Override
	public TraineeResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainee by id={} from DAO", id);
		final var trainee = getById(id);
		return mapper.traineeToTraineeResponseDTO(trainee);
	}

	@Override
	public List<TraineeResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainees from DAO");
		return mapper.traineeListToTraineeResponseDTOList(traineeDAO.findAll());
	}

	@Override
	public TraineeResponseDTO update(final TraineeRequestDTO request) {
		LOGGER.debug("In update - Updating trainee from request {}", request);
		final var trainee = getById(request.getId());
		trainee.setFirstName(request.getFirstName());
		trainee.setLastName(request.getLastName());
		trainee.setUserName(userCredentialsGenerator.generateUserName(trainee));
		trainee.setActive(request.isActive());
		trainee.setDateOfBirth(request.getDateOfBirth());
		trainee.setAddress(request.getAddress());
		return mapper.traineeToTraineeResponseDTO(traineeDAO.update(trainee));
	}

	@Override
	public void delete(final Long id) {
		LOGGER.debug("In delete - fetching trainings before removing trainee by id={}", id);
		final List<Training> trainings = trainingDAO.findAll().stream()
			.filter(t -> id.equals(t.getTrainee().getId()))
			.toList();
		for (var training : trainings) {
			trainingDAO.delete(training.getId());
		}
		LOGGER.debug("In delete - removed {} trainings, removing trainee", trainings.size());
		traineeDAO.delete(id);
	}

	private Trainee getById(final Long id) {
		return traineeDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_MESSAGE + id));
	}
}
