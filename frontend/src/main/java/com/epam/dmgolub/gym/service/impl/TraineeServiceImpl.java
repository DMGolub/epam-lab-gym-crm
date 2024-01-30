package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.UpdateTrainerListRequestDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.Trainee;
import com.epam.dmgolub.gym.service.TraineeService;
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
public class TraineeServiceImpl implements TraineeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);

	private final TrainingTypeService trainingTypeService;
	private final RestTemplate restTemplate;
	private final ModelToDtoMapper mapper;

	@Value("${backend.url}")
	private String backendUrl;

	public TraineeServiceImpl(
		final TrainingTypeService trainingTypeService,
		final RestTemplate restTemplate,
		final ModelToDtoMapper mapper
	) {
		this.trainingTypeService = trainingTypeService;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}

	@Override
	public CredentialsDTO save(final TraineeRequestDTO trainee) {
		LOGGER.debug("In save - Received a request to save trainee={}", trainee);
		final String requestUrl = backendUrl + "/api/v1/trainees";
		final HttpEntity<TraineeRequestDTO> requestEntity = new HttpEntity<>(trainee);
		final var credentials =
			restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, CredentialsDTO.class).getBody();
		LOGGER.debug("In save - Trainee saved successfully, returning user credentials");
		return credentials;
	}

	@Override
	public TraineeResponseDTO findByUserName(final String userName, final HttpSession session) {
		LOGGER.debug("In findByUserName - Fetching trainee from backend by userName={}", userName);
		final String requestUrl = backendUrl + "/api/v1/trainees/profile?userName=" + userName;
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders(session));
		final var trainee =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Trainee.class).getBody();
		if (trainee != null) {
			return convertToTraineeResponseDTO(trainee);
		} else {
			throw new EntityNotFoundException("Can not find trainee by username=" + userName);
		}
	}

	@Override
	public List<TraineeResponseDTO> findAll(final HttpSession session) {
		LOGGER.debug("In findAll - Fetching all trainees from backend");
		final String requestUrl = backendUrl + "/api/v1/trainees";
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeaders(session));
		final var trainees =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Trainee[].class).getBody();
		final List<TraineeResponseDTO> result = new ArrayList<>();
		if (trainees != null) {
			for (var trainee : trainees) {
				result.add(convertToTraineeResponseDTO(trainee));
			}
		}
		return result;
	}

	@Override
	public void delete(final String userName, final HttpSession session) {
		LOGGER.debug("In delete - Sending request to delete trainee with userName={}", userName);
		final String deleteUrl = backendUrl + "/api/v1/trainees/profile?userName=" + userName;
		final HttpEntity<?> requestEntity = new HttpEntity<>(getAuthHeaders(session));
		restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, String.class);
	}

	@Override
	public void update(final TraineeRequestDTO trainee, final HttpSession session) {
		LOGGER.debug("In save - Received a request to update trainee={}", trainee);
		final String requestUrl = backendUrl + "/api/v1/trainees/profile";
		final HttpEntity<TraineeRequestDTO> requestEntity = new HttpEntity<>(trainee, getAuthHeaders(session));
		restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, Trainee.class);
	}

	@Override
	public void addTrainer(final String traineeUserName, final String trainerUserName, final HttpSession session) {
		LOGGER.debug("In addTrainer - Received a request to add trainer {} to trainee {}",
			trainerUserName, traineeUserName);
		final List<String> trainerUserNames = new ArrayList<>();
		findByUserName(traineeUserName, session)
			.getTrainers()
			.forEach(trainer -> trainerUserNames.add(trainer.getUserName()));
		trainerUserNames.add(trainerUserName);
		final var request = new UpdateTrainerListRequestDTO(traineeUserName, trainerUserNames);
		final String requestUrl = backendUrl + "/api/v1/trainees/profile/update-trainers";
		final HttpEntity<UpdateTrainerListRequestDTO> requestEntity = new HttpEntity<>(request, getAuthHeaders(session));
		restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, Trainee.Trainer[].class);
	}

	private TraineeResponseDTO convertToTraineeResponseDTO(final Trainee trainee) {
		final var traineeDTO = mapper.mapToTraineeResponseDTO(trainee);
		for (var trainer : trainee.getTrainers()) {
			traineeDTO.getTrainers().add(convertToTrainerDTO(trainer));
		}
		return traineeDTO;
	}

	private TraineeResponseDTO.TrainerDTO convertToTrainerDTO(final Trainee.Trainer trainer) {
		final var trainerDTO = mapper.mapToTraineeResponseDTOTrainerDTO(trainer);
		final var trainingType = trainingTypeService.findByLink(trainer.getSpecialization());
		trainerDTO.setSpecialization(trainingType);
		return trainerDTO;
	}
}
