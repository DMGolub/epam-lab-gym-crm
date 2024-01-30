package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.controller.TrainingTypeRestController;
import com.epam.dmgolub.gym.dto.TraineeTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingResponseDTO;
import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.TrainerCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerUpdateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.dto.TraineeCreateRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeUpdateRequestDTO;
import com.epam.dmgolub.gym.model.ChangePasswordRequest;
import com.epam.dmgolub.gym.model.Credentials;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TrainerModel;
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

	public abstract Credentials mapToCredentials(final CredentialsDTO credentialsDTO);
}
