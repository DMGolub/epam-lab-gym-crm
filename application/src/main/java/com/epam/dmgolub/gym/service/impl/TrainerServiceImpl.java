package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainerDAO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

	private static final String TRAINER_NOT_FOUND_MESSAGE = "Can not find trainer by id=";
	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);

	@Autowired
	private TrainerDAO trainerDAO;
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
	public TrainerResponseDTO save(final TrainerRequestDTO request) {
		LOGGER.debug("In save - saving trainer from request {}", request);
		final var trainer = mapper.trainerRequestDTOToTrainer(request);
		trainer.setUserName(userCredentialsGenerator.generateUserName(trainer));
		trainer.setPassword(userCredentialsGenerator.generatePassword(trainer));
		return mapper.trainerToTrainerResponseDTO(trainerDAO.save(trainer));
	}

	@Override
	public TrainerResponseDTO findById(final Long id) {
		LOGGER.debug("In findById - Fetching trainer by id={} from DAO", id);
		final var trainer = getById(id);
		return mapper.trainerToTrainerResponseDTO(trainer);
	}

	@Override
	public List<TrainerResponseDTO> findAll() {
		LOGGER.debug("In findAll - Fetching all trainers from DAO");
		return mapper.trainerListToTrainerResponseDTOList(trainerDAO.findAll());
	}

	@Override
	public TrainerResponseDTO update(final TrainerRequestDTO request) {
		LOGGER.debug("In update - Updating trainer from request {}", request);
		final var trainer = getById(request.getId());
		trainer.setFirstName(request.getFirstName());
		trainer.setLastName(request.getLastName());
		trainer.setUserName(userCredentialsGenerator.generateUserName(trainer));
		trainer.setActive(request.isActive());
		trainer.setSpecialization(mapper.trainingTypeDTOToTrainingType(request.getSpecialization()));
		return mapper.trainerToTrainerResponseDTO(trainerDAO.update(trainer));
	}

	private Trainer getById(final Long id) {
		return trainerDAO.findById(id).orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_MESSAGE + id));
	}
}
