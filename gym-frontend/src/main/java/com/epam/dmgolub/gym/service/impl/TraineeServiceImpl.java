package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.SignUpResponseDTO;
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

import static com.epam.dmgolub.gym.service.constant.Constants.API;
import static com.epam.dmgolub.gym.service.constant.Constants.PROFILE_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAINEES_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.VERSION_V1;
import static com.epam.dmgolub.gym.service.utility.ServiceUtilities.getAuthHeader;

@Service
public class TraineeServiceImpl implements TraineeService {

	private static final String TRAINEES_BASE_URL = API + VERSION_V1 + TRAINEES_LOCATION;
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
	public SignUpResponseDTO save(final TraineeRequestDTO trainee) {
		LOGGER.debug("In save - Received a request to save trainee={}", trainee);
		final String requestUrl = backendUrl + TRAINEES_BASE_URL;
		LOGGER.debug("In save - Sending POST request to {}", requestUrl);
		final HttpEntity<TraineeRequestDTO> requestEntity = new HttpEntity<>(trainee);
		final var signUpResponseDTO =
			restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, SignUpResponseDTO.class).getBody();
		LOGGER.debug("In save - Trainee saved successfully, returning user credentials and token");
		return signUpResponseDTO;
	}

	@Override
	public TraineeResponseDTO findByUserName(final String userName, final HttpSession session) {
		LOGGER.debug("In findByUserName - Received a request to find trainee by userName={}", userName);
		final String requestUrl = backendUrl + TRAINEES_BASE_URL + PROFILE_LOCATION + "?userName=" + userName;
		LOGGER.debug("In findByUserName - Sending GET request to {}", requestUrl);
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
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
		LOGGER.debug("In findAll - Received a request to find all trainees");
		final String requestUrl = backendUrl + TRAINEES_BASE_URL;
		LOGGER.debug("In findAll - Sending GET request to {}", requestUrl);
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
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
		LOGGER.debug("In delete - Received a request to delete trainee with userName={}", userName);
		final String requestUrl = backendUrl + TRAINEES_BASE_URL + PROFILE_LOCATION + "?userName=" + userName;
		LOGGER.debug("In delete - Sending DELETE request to {}", requestUrl);
		final HttpEntity<?> requestEntity = new HttpEntity<>(getAuthHeader(session));
		restTemplate.exchange(requestUrl, HttpMethod.DELETE, requestEntity, String.class);
	}

	@Override
	public void update(final TraineeRequestDTO trainee, final HttpSession session) {
		LOGGER.debug("In update - Received a request to update trainee={}", trainee);
		final String requestUrl = backendUrl + TRAINEES_BASE_URL + PROFILE_LOCATION;
		LOGGER.debug("In update - Sending PUT request to {}", requestUrl);
		final HttpEntity<TraineeRequestDTO> requestEntity = new HttpEntity<>(trainee, getAuthHeader(session));
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
		final String requestUrl = backendUrl + TRAINEES_BASE_URL + PROFILE_LOCATION + "/update-trainers";
		LOGGER.debug("In addTrainer - Sending PUT request to {}", requestUrl);
		final HttpEntity<UpdateTrainerListRequestDTO> requestEntity = new HttpEntity<>(request, getAuthHeader(session));
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
