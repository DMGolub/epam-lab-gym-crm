package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.mapper.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.Training;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.service.constant.Constants.API;
import static com.epam.dmgolub.gym.service.constant.Constants.TRAININGS_LOCATION;
import static com.epam.dmgolub.gym.service.constant.Constants.VERSION_V1;
import static com.epam.dmgolub.gym.service.utility.ServiceUtilities.getAuthHeader;

@Service
public class TrainingServiceImpl implements TrainingService {

	private static final String TRAININGS_BASE_URL = API + VERSION_V1 + TRAININGS_LOCATION;

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

	private final TraineeService traineeService;
	private final TrainerService trainerService;
	private final RestTemplate restTemplate;
	private final ModelToDtoMapper mapper;

	@Value("${backend.url}")
	private String backendUrl;
	private final Format formatter = new SimpleDateFormat("yyyy-MM-dd");

	public TrainingServiceImpl(
		final TraineeService traineeService,
		final TrainerService trainerService,
		final RestTemplate restTemplate,
		final ModelToDtoMapper mapper
	) {
		this.traineeService = traineeService;
		this.trainerService = trainerService;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}

	@Override
	public void save(final TrainingCreateRequestDTO training, final HttpSession session) {
		LOGGER.debug("In save - Received a request to save training={}", training);
		final var requestUrl = backendUrl + TRAININGS_BASE_URL;
		LOGGER.debug("In save - Sending POST request to {}", requestUrl);
		final HttpEntity<TrainingCreateRequestDTO> requestEntity = new HttpEntity<>(training, getAuthHeader(session));
		restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, Training.class);
	}

	@Override
	public List<TrainingResponseDTO> searchByTrainee(
		final TraineeTrainingsSearchRequestDTO request,
		final HttpSession session
	) {
		LOGGER.debug("In searchByTrainee - Received a request to find trainings={}", request);
		final var requestUrl = generateRequestUrl(request);
		LOGGER.debug("In searchByTrainee - Sending GET request to {}", requestUrl);
		final HttpEntity<TraineeTrainingsSearchRequestDTO> requestEntity =
			new HttpEntity<>(request, getAuthHeader(session));
		final var trainings =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Training[].class).getBody();
		final List<TrainingResponseDTO> result = new ArrayList<>();
		if (trainings != null) {
			for (var training : trainings) {
				training.setTraineeUserName(request.getTraineeUserName());
				result.add(mapToTrainingResponseDTO(training, session));
			}
		}
		return result;
	}

	@Override
	public List<TrainingResponseDTO> searchByTrainer(
		final TrainerTrainingsSearchRequestDTO request,
		final HttpSession session
	) {
		LOGGER.debug("In searchByTrainer - Received a request to find trainings={}", request);
		final var requestUrl = generateRequestUrl(request);
		LOGGER.debug("In searchByTrainer - Sending GET request to {}", requestUrl);
		final HttpEntity<Void> requestEntity = new HttpEntity<>(getAuthHeader(session));
		final var trainings =
			restTemplate.exchange(requestUrl, HttpMethod.GET, requestEntity, Training[].class).getBody();
		final List<TrainingResponseDTO> result = new ArrayList<>();
		if (trainings != null) {
			for (var training : trainings) {
				training.setTrainerUserName(request.getTrainerUserName());
				result.add(mapToTrainingResponseDTO(training, session));
			}
		}
		return result;
	}

	private String generateRequestUrl(final TrainerTrainingsSearchRequestDTO request) {
		final var periodFrom = request.getPeriodFrom();
		final var periodTo = request.getPeriodTo();
		final var traineeUserName = request.getTraineeUserName();

		return backendUrl + TRAININGS_BASE_URL + "/search-by-trainer" +
			"?trainerUserName=" + request.getTrainerUserName() +
			(periodFrom != null ? "&periodFrom=" + formatter.format(periodFrom) : "") +
			(periodTo != null ? "&periodTo=" + formatter.format(periodTo) : "") +
			(traineeUserName != null && !traineeUserName.isBlank()? "&traineeUserName=" + traineeUserName : "");
	}

	private String generateRequestUrl(final TraineeTrainingsSearchRequestDTO request) {
		final var periodFrom = request.getPeriodFrom();
		final var periodTo = request.getPeriodTo();
		final var trainerUserName = request.getTrainerUserName();
		final var trainingType = request.getType();

		return backendUrl + TRAININGS_BASE_URL + "/search-by-trainee" +
			"?traineeUserName=" + request.getTraineeUserName() +
			(periodFrom != null ? "&periodFrom=" + formatter.format(periodFrom) : "") +
			(periodTo != null ? "&periodTo=" + formatter.format(periodTo) : "") +
			(trainerUserName != null && !trainerUserName.isBlank() ? "&trainerUserName=" + trainerUserName : "") +
			(trainingType != null ? "&trainingTypeId=" + trainingType.getId() : "");
	}

	private TrainingResponseDTO mapToTrainingResponseDTO(final Training training, final HttpSession session) {
		final var responseDTO = mapper.mapToTrainingResponseDTO(training);
		responseDTO.setTrainee(traineeService.findByUserName(training.getTraineeUserName(), session));
		responseDTO.setTrainer(trainerService.findByUserName(training.getTrainerUserName(), session));
		return responseDTO;
	}
}
