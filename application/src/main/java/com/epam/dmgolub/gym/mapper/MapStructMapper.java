package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingTypeDTO;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MapStructMapper {

	protected TraineeRepository traineeRepository;
	protected TrainerRepository trainerRepository;

	@Autowired
	public void setTraineeRepository(final TraineeRepository traineeRepository) {
		this.traineeRepository = traineeRepository;
	}

	@Autowired
	public void setTrainerRepository(final TrainerRepository trainerRepository) {
		this.trainerRepository = trainerRepository;
	}

	@Mapping(target = "userId", source = "trainee.user.id")
	@Mapping(target = "firstName", source = "trainee.user.firstName")
	@Mapping(target = "lastName", source = "trainee.user.lastName")
	@Mapping(target = "userName", source = "trainee.user.userName")
	@Mapping(target = "active", source = "trainee.user.active")
	public abstract TraineeResponseDTO traineeToTraineeResponseDTO(Trainee trainee);

	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	public abstract TraineeResponseDTO.TrainerDTO trainerToTraineeResponseDTOTrainerDTO(Trainer trainer);

	public abstract List<TraineeResponseDTO> traineeListToTraineeResponseDTOList(List<Trainee> traineeList);
	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainers", ignore = true)
	public abstract Trainee traineeRequestDTOToTrainee(TraineeRequestDTO traineeRequestDTO);

	public abstract TrainingTypeDTO trainingTypeToTrainingTypeDTO(TrainingType trainingType);

	public abstract List<TrainingTypeDTO> trainingTypeListToTrainingTypeDTOList(List<TrainingType> trainingTypes);

	public abstract TrainingType trainingTypeDTOToTrainingType(TrainingTypeDTO trainingTypeDTO);

	@Mapping(target = "userId", source = "trainer.user.id")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "active", source = "trainer.user.active")
	public abstract TrainerResponseDTO trainerToTrainerResponseDTO(Trainer trainer);

	public abstract List<TrainerResponseDTO> trainerListToTrainerResponseDTOList(List<Trainer> trainerList);
	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainees", ignore = true)
	public abstract Trainer trainerRequestDTOToTrainer(TrainerRequestDTO trainerRequestDTO);

	public abstract TrainingResponseDTO trainingToTrainingResponseDTO(Training training);

	public abstract List<TrainingResponseDTO> trainingListToTrainingResponseDTOList(List<Training> trainingList);

	@Mapping(target = "trainee", expression = "java(traineeRepository.findById(trainingRequestDTO.getTraineeId()).get())")
	@Mapping(target = "trainer", expression = "java(trainerRepository.findById(trainingRequestDTO.getTrainerId()).get())")
	public abstract Training trainingRequestDTOToTraining(TrainingRequestDTO trainingRequestDTO);
}
