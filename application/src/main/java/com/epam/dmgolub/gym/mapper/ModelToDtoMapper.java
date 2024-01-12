package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.dto.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
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

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ModelToDtoMapper {

	public abstract TraineeResponseDTO traineeToTraineeResponseDTO(TraineeModel trainee);

	public abstract TraineeResponseDTO.TrainerDTO trainerToTraineeResponseDTOTrainerDTO(TrainerModel trainer);

	public abstract List<TraineeResponseDTO> traineeListToTraineeResponseDTOList(List<TraineeModel> traineeList);

	@Mapping(target = "userName", ignore = true)
	@Mapping(target = "trainers", ignore = true)
	public abstract TraineeModel traineeRequestDTOToTrainee(TraineeRequestDTO traineeRequestDTO);

	public abstract TrainingTypeDTO trainingTypeToTrainingTypeDTO(TrainingTypeModel trainingType);

	public abstract List<TrainingTypeDTO> trainingTypeListToTrainingTypeDTOList(List<TrainingTypeModel> trainingTypes);

	public abstract TrainingTypeModel trainingTypeDTOToTrainingType(TrainingTypeDTO trainingTypeDTO);

	public abstract TrainerResponseDTO trainerToTrainerResponseDTO(TrainerModel trainer);

	public abstract List<TrainerResponseDTO> trainerListToTrainerResponseDTOList(List<TrainerModel> trainerList);

	@Mapping(target = "userName", ignore = true)
	public abstract TrainerModel trainerRequestDTOToTrainer(TrainerRequestDTO trainerRequestDTO);

	public abstract TrainingResponseDTO trainingToTrainingResponseDTO(TrainingModel training);

	public abstract List<TrainingResponseDTO> trainingListToTrainingResponseDTOList(List<TrainingModel> trainingList);

	@Mapping(target = "trainee", source = "traineeId")
	@Mapping(target = "trainer", source = "trainerId")
	@Mapping(target = "type", ignore = true)
	public abstract TrainingModel trainingRequestDTOToTraining(TrainingRequestDTO trainingRequestDTO);

	protected TraineeModel idToTraineeModel(final Long traineeId) {
		final var trainee = new TraineeModel();
		trainee.setId(traineeId);
		return trainee;
	}

	protected TrainerModel idToTrainerModel(final Long trainerId) {
		final var trainer = new TrainerModel();
		trainer.setId(trainerId);
		return trainer;
	}

	public abstract TraineeTrainingsSearchRequest searchRequestDTOToSearchRequest(TraineeTrainingsSearchRequestDTO request);

	public abstract TrainerTrainingsSearchRequest searchRequestDTOToSearchRequest(TrainerTrainingsSearchRequestDTO request);

	public abstract Credentials credentialsDTOTOCredentials(CredentialsDTO credentialsDTO);

	public abstract ChangePasswordRequest changePasswordRequestDTOToRequest(ChangePasswordRequestDTO requestDTO);
}
