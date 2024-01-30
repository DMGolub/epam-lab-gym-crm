package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
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

import static com.epam.dmgolub.gym.service.utility.ServiceUtilities.getAuthHeaders;

@Service
public class TrainerServiceImpl implements TrainerService {

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
	public CredentialsDTO save(final TrainerRequestDTO trainer) {
		LOGGER.debug("In save - Received a request to save trainer={}", trainer);
		final String requestUrl = backendUrl + "/api/v1/trainers";
		final HttpEntity<Trainer> requestEntity = new HttpEntity<>(mapper.mapToTrainerModel(trainer));
		final var credentials =
			restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, CredentialsDTO.class).getBody();
		LOGGER.debug("In save - Trainer saved successfully, returning user credentials");
		return credentials;
	}

	@Override
	public TrainerResponseDTO findByUserName(final String userName, final HttpSession session) {
		LOGGER.debug("In findByUserName - Fetching trainer from backend by userName={}", userName);
		final String requestUrl = backendUrl + "/api/v1/trainers/profile?userName=" + userName;
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders(session));
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
		LOGGER.debug("In findAll - Fetching all trainers from backend");
		final String requestUrl = backendUrl + "/api/v1/trainers";
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders(session));
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
		final String requestUrl = backendUrl + "/api/v1/trainers/profile";
		final var trainer = mapper.mapToTrainerModel(trainerDTO);
		final HttpEntity<Trainer> requestEntity = new HttpEntity<>(trainer, getAuthHeaders(session));
		restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, Trainer.class);
	}

	@Override
	public List<TraineeResponseDTO.TrainerDTO> findActiveTrainersNotAssignedOnTrainee(
		final String userName,
		final HttpSession session
	) {
		LOGGER.debug("In findActiveTrainersNotAssignedOnTrainee - Received a request to find by userName={}", userName);
		final String requestUrl = backendUrl + "/api/v1/trainers/not-assigned-on?userName=" + userName;
		return getActiveTrainers(requestUrl, session);
	}

	@Override
	public List<TraineeResponseDTO.TrainerDTO> findActiveTrainersAssignedOnTrainee(
		final String userName,
		final HttpSession session
	) {
		LOGGER.debug("In findActiveTrainersAssignedOnTrainee - Received a request to find by userName={}", userName);
		final String requestUrl = backendUrl + "/api/v1/trainers/assigned-on?userName=" + userName;
		return getActiveTrainers(requestUrl, session);
	}

	private List<TraineeResponseDTO.TrainerDTO> getActiveTrainers(final String requestUrl, final HttpSession session) {
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders(session));
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
