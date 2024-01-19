package com.epam.dmgolub.gym.mapper.mvc;

import com.epam.dmgolub.gym.dto.mvc.ChangePasswordRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.CredentialsDTO;
import com.epam.dmgolub.gym.dto.mvc.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.mvc.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingResponseDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingTypeDTO;
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

	public abstract TraineeResponseDTO mapToTraineeResponseDTO(TraineeModel trainee);

	public abstract List<TraineeResponseDTO> mapToTraineeResponseDTOList(List<TraineeModel> traineeList);

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "trainers", ignore = true)
	public abstract TraineeModel mapToTraineeModel(TraineeRequestDTO traineeRequestDTO);

	public abstract TrainingTypeDTO mapToTrainingTypeDTO(TrainingTypeModel trainingType);

	public abstract List<TrainingTypeDTO> mapToTrainingTypeDTOList(List<TrainingTypeModel> trainingTypes);

	public abstract TrainingTypeModel mapToTrainingTypeModel(TrainingTypeDTO trainingTypeDTO);

	public abstract TrainerResponseDTO mapToTrainerResponseDTO(TrainerModel trainer);

	public abstract List<TrainerResponseDTO> mapToTrainerResponseDTOList(List<TrainerModel> trainerList);

	@Mapping(target = "password", ignore = true)
	@Mapping(target = "trainees", ignore = true)
	public abstract TrainerModel mapToTrainerModel(TrainerRequestDTO trainerRequestDTO);

	public abstract TrainingResponseDTO mapTrainingResponseDTO(TrainingModel training);

	public abstract List<TrainingResponseDTO> mapToTrainingResponseDTOList(List<TrainingModel> trainingList);

	@Mapping(target = "trainee", source = "traineeUserName")
	@Mapping(target = "trainer", source = "trainerUserName")
	@Mapping(target = "type", ignore = true)
	public abstract TrainingModel mapToTrainingModel(TrainingRequestDTO trainingRequestDTO);

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

	public abstract TraineeTrainingsSearchRequest mapToTraineeTrainingsSearchRequest(TraineeTrainingsSearchRequestDTO request);

	public abstract TrainerTrainingsSearchRequest mapToTrainerTrainingsSearchRequest(TrainerTrainingsSearchRequestDTO request);

	public abstract Credentials mapToCredentials(CredentialsDTO credentialsDTO);

	public abstract ChangePasswordRequest mapToChangePasswordRequest(ChangePasswordRequestDTO requestDTO);
}
