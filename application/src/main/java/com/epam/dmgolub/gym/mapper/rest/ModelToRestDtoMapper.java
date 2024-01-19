package com.epam.dmgolub.gym.mapper.rest;

import com.epam.dmgolub.gym.controller.rest.TrainingTypeRestController;
import com.epam.dmgolub.gym.dto.rest.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.rest.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.rest.CredentialsDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TrainerUpdateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TrainingTypeDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.rest.TraineeUpdateRequestDTO;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ModelToRestDtoMapper {

	public abstract TraineeResponseDTO mapToTraineeResponseDTO(TraineeModel trainee);

	public abstract List<TraineeResponseDTO> mapToTraineeResponseDTOList(List<TraineeModel> trainees);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "userName", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trainers", ignore = true)
	public abstract TraineeModel mapToTraineeModel(TraineeCreateRequestDTO request);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trainers", ignore = true)
	public abstract TraineeModel mapToTraineeModel(TraineeUpdateRequestDTO request);

	public abstract TrainingTypeDTO mapToTrainingTypeDTO(final TrainingTypeModel trainingType);

	protected String mapToReference(final TrainingTypeModel trainingType) {
		return UriComponentsBuilder.fromPath(TrainingTypeRestController.URL + "/{id}")
			.buildAndExpand(trainingType.getId()).toUriString();
	}

	public abstract List<TrainingTypeDTO> mapToTrainingTypeDTOList(final List<TrainingTypeModel> types);

	public abstract TrainerResponseDTO mapToTrainerResponseDTO(TrainerModel trainer);

	public abstract List<TrainerResponseDTO> mapToTrainerResponseDTOList(List<TrainerModel> trainers);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "userName", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trainees", ignore = true)
	public abstract TrainerModel mapToTrainerModel(TrainerCreateRequestDTO request);

	protected TrainingTypeModel mapReferenceToTrainingType(final String reference) {
		if (reference == null) {
			return null;
		}
		final String id = reference.substring(reference.lastIndexOf('/') + 1);
		final var trainingType = new TrainingTypeModel();
		trainingType.setId(Long.valueOf(id));
		return trainingType;
	}

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trainees", ignore = true)
	public abstract TrainerModel mapToTrainerModel(TrainerUpdateRequestDTO request);

	public abstract ChangePasswordRequest mapToChangePasswordRequest(ChangePasswordRequestDTO request);

	@Mapping(target = "trainerUserName", source = "training.trainer.userName")
	public abstract TraineeTrainingResponseDTO mapToTraineeTrainingResponseDTO(TrainingModel training);

	public abstract List<TraineeTrainingResponseDTO> mapToTraineeTrainingResponseDTOList(List<TrainingModel> trainings);

	@Mapping(target = "traineeUserName", source = "training.trainee.userName")
	public abstract TrainerTrainingResponseDTO mapToTrainerTrainingResponseDTO(TrainingModel training);

	public abstract List<TrainerTrainingResponseDTO> mapToTrainerTrainingResponseDTOList(List<TrainingModel> trainings);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "trainee", source = "traineeUserName")
	@Mapping(target = "trainer", source = "trainerUserName")
	@Mapping(target = "type", ignore = true)
	public abstract TrainingModel mapToTrainingModel(TrainingCreateRequestDTO request);

	protected TraineeModel mapToTraineeModel(final String traineeUserName) {
		final var trainee = new TraineeModel();
		trainee.setUserName(traineeUserName);
		return trainee;
	}

	protected TrainerModel mapToTrainerModel(final String trainerUserName) {
		final var trainer = new TrainerModel();
		trainer.setUserName(trainerUserName);
		return trainer;
	}

	public abstract TraineeResponseDTO.TrainerDTO mapToTraineeResponseDTOTrainerDTO(TrainerModel trainer);

	public abstract List<TraineeResponseDTO.TrainerDTO> mapToTraineeResponseDTOTrainerDTOList(List<TrainerModel> trainer);

	public abstract List<TraineeResponseDTO.TrainerDTO> mapTraineeModelTrainerListToTraineeResponseDTOTrainerDTOList(List<TraineeModel.Trainer> trainer);

	@Mapping(target = "traineeUserName", source = "request.userName")
	public abstract TraineeTrainingsSearchRequest mapToTraineeTrainingsSearchRequest(TraineeTrainingsSearchRequestDTO request);

	@Mapping(target = "trainerUserName", source = "request.userName")
	public abstract TrainerTrainingsSearchRequest mapToTrainerTrainingsSearchRequest(TrainerTrainingsSearchRequestDTO request);

	public abstract Credentials mapToCredentials(final CredentialsDTO credentialsDTO);
}
