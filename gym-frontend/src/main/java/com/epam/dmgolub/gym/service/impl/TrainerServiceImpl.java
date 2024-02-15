package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.Trainee;
import com.epam.dmgolub.gym.model.Trainer;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.API;
import static com.epam.dmgolub.gym.service.constant.Constants.PROFILE_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINERS_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.VERSION_V1;
import static com.epam.dmgolub.gym.service.utility.ServiceUtilities.getAuthHeader;

@Service
public class TrainerServiceImpl implements TrainerService {

	private static final String TRAINERS_BASE_URL = API + VERSION_V1 + TRAINERS_LOCATION;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);

	private final TrainingTypeService trainingTypeService;
	private final RestTemplate restTemplate;
	private final ModelToDtoMapper mapper;

	@Value("${backend.url}")
	private String backendUrl;

	public TrainerServiceImpl(
		final TrainingTypeService trainingTypeService, 
		final RestTemplate restTemplate, 
		final ModelToDtoMapper mapper
	) {
		this.trainingTypeService = trainingTypeService;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}

	@Override
	public SignUpResponseDTO save(final TrainerRequestDTO trainer) {
		LOGGER.debug("In save - Received a request to save trainer={}", trainer);
		final String requestUrl = backendUrl + TRAINERS_BASE_URL;
		LOGGER.debug("In save - Sending POST request to {}", requestUrl);
		final HttpEntity<Trainer> requestEntity = new HttpEntity<>(mapper.mapToTrainerModel(trainer));
		final var signUpResponseDTO =
			restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, SignUpResponseDTO.class).getBody();
		LOGGER.debug("In save - Trainer saved successfully, returning user credentials and token");
		return signUpResponseDTO;
	}

	@Override
	public TrainerResponseDTO findByUserName(final String userName, final HttpSession session) {
		LOGGER.debug("In findByUserName - Received a request to find trainer by userName={}", userName);
		final String requestUrl = backendUrl + TRAINERS_BASE_URL + PROFILE_LOCATION + "?userName=" + userName;
		LOGGER.debug("In findByUserName - Sending GET request to {}", requestUrl);
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
		final var trainer =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Trainer.class).getBody();
		if (trainer != null) {
			return convertToTrainerResponseDTO(trainer);
		} else {
			throw new EntityNotFoundException("Can not find trainer by userName=" + userName);
		}
	}

	@Override
	public List<TrainerResponseDTO> findAll(final HttpSession session) {
		LOGGER.debug("In findAll - Received a request to find all trainers");
		final String requestUrl = backendUrl + TRAINERS_BASE_URL;
		LOGGER.debug("In findAll - Sending GET request to {}", requestUrl);
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
		final var trainers =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Trainer[].class).getBody();
		final List<TrainerResponseDTO> result = new ArrayList<>();
		if (trainers != null) {
			for (var trainer : trainers) {
				result.add(convertToTrainerResponseDTO(trainer));
			}
		}
		return result;
	}

	@Override
	public void update(final TrainerRequestDTO trainerDTO, final HttpSession session) {
		LOGGER.debug("In save - Received a request to update trainer={}", trainerDTO);
		final String requestUrl = backendUrl + TRAINERS_BASE_URL + PROFILE_LOCATION;
		final var trainer = mapper.mapToTrainerModel(trainerDTO);
		LOGGER.debug("In update - Sending PUT request to {}", requestUrl);
		final HttpEntity<Trainer> requestEntity = new HttpEntity<>(trainer, getAuthHeader(session));
		restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, Trainer.class);
	}

	@Override
	public List<TraineeResponseDTO.TrainerDTO> findActiveTrainersNotAssignedOnTrainee(
		final String userName,
		final HttpSession session
	) {
		LOGGER.debug("In findActiveTrainersNotAssignedOnTrainee - Received a request to find by userName={}", userName);
		final String requestUrl = backendUrl + TRAINERS_BASE_URL + "/not-assigned-on?userName=" + userName;
		LOGGER.debug("In findActiveTrainersNotAssignedOnTrainee - Sending GET request to {}", requestUrl);
		return getActiveTrainers(requestUrl, session);
	}

	@Override
	public List<TraineeResponseDTO.TrainerDTO> findActiveTrainersAssignedOnTrainee(
		final String userName,
		final HttpSession session
	) {
		LOGGER.debug("In findActiveTrainersAssignedOnTrainee - Received a request to find by userName={}", userName);
		final String requestUrl = backendUrl + TRAINERS_BASE_URL + "/assigned-on?userName=" + userName;
		LOGGER.debug("In findActiveTrainersAssignedOnTrainee - Sending GET request to {}", requestUrl);
		return getActiveTrainers(requestUrl, session);
	}

	private List<TraineeResponseDTO.TrainerDTO> getActiveTrainers(final String requestUrl, final HttpSession session) {
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
		final var trainers =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Trainee.Trainer[].class).getBody();
		final List<TraineeResponseDTO.TrainerDTO> result = new ArrayList<>();
		if (trainers != null) {
			for (var trainer : trainers) {
				result.add(convertToTraineeResponseDTOTrainerDTO(trainer));
			}
		}
		return result;
	}

	private TrainerResponseDTO convertToTrainerResponseDTO(final Trainer trainer) {
		final var trainerDTO = mapper.mapToTrainerResponseDTO(trainer);
		final var trainingType = trainingTypeService.findByLink(trainer.getSpecialization());
		trainerDTO.setSpecialization(trainingType);
		return trainerDTO;
	}

	private TraineeResponseDTO.TrainerDTO convertToTraineeResponseDTOTrainerDTO(final Trainee.Trainer trainer) {
		final var trainerDTO = mapper.mapToTraineeResponseDTOTrainerDTO(trainer);
		final var trainingType = trainingTypeService.findByLink(trainer.getSpecialization());
		trainerDTO.setSpecialization(trainingType);
		return trainerDTO;
	}
}
