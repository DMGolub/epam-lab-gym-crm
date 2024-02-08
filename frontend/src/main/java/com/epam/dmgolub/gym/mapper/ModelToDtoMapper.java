package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.model.Trainee;
import com.epam.dmgolub.gym.model.Trainer;
import com.epam.dmgolub.gym.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ModelToDtoMapper {

	@Mapping(target = "trainers", ignore = true)
	public abstract TraineeResponseDTO mapToTraineeResponseDTO(Trainee traineeModel);

	@Mapping(target = "specialization", ignore = true)
	public abstract TraineeResponseDTO.TrainerDTO mapToTraineeResponseDTOTrainerDTO(Trainee.Trainer trainer);

	@Mapping(target = "specialization", ignore = true)
	public abstract TrainerResponseDTO mapToTrainerResponseDTO(Trainer trainer);

	@Mapping(target = "trainee", ignore = true)
	@Mapping(target = "trainer", ignore = true)
	public abstract TrainingResponseDTO mapToTrainingResponseDTO(Training training);

	public abstract Trainer mapToTrainerModel(TrainerRequestDTO trainer);

	protected String mapTrainingType(final TrainingTypeDTO trainingType) {
		return "/api/v1/training-types/" + trainingType.getId();
	}
}
