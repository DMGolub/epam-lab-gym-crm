package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.dao.TraineeDAO;
import com.epam.dmgolub.gym.dao.TrainerDAO;
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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MapStructMapper {

	protected TraineeDAO traineeDAO;
	protected TrainerDAO trainerDAO;

	@Autowired
	public void setTraineeDAO(final TraineeDAO traineeDAO) {
		this.traineeDAO = traineeDAO;
	}

	@Autowired
	public void setTrainerDAO(final TrainerDAO trainerDAO) {
		this.trainerDAO = trainerDAO;
	}

	public abstract TraineeResponseDTO traineeToTraineeResponseDTO(Trainee trainee);
	public abstract List<TraineeResponseDTO> traineeListToTraineeResponseDTOList(List<Trainee> traineeList);
	@Mapping(target = "userName", ignore = true)
	@Mapping(target = "password", ignore = true)
	public abstract Trainee traineeRequestDTOToTrainee(TraineeRequestDTO traineeRequestDTO);

	public abstract TrainingTypeDTO trainingTypeToTrainingTypeDTO(TrainingType trainingType);
	public abstract List<TrainingTypeDTO> trainingTypeListToTrainingTypeDTOList(List<TrainingType> trainingTypes);
	public abstract TrainingType trainingTypeDTOToTrainingType(TrainingTypeDTO trainingTypeDTO);

	public abstract TrainerResponseDTO trainerToTrainerResponseDTO(Trainer trainer);
	public abstract List<TrainerResponseDTO> trainerListToTrainerResponseDTOList(List<Trainer> trainerList);
	@Mapping(target = "userName", ignore = true)
	@Mapping(target = "password", ignore = true)
	public abstract Trainer trainerRequestDTOToTrainer(TrainerRequestDTO trainerRequestDTO);

	public abstract TrainingResponseDTO trainingToTrainingResponseDTO(Training training);
	public abstract List<TrainingResponseDTO> trainingListToTrainingResponseDTOList(List<Training> trainingList);
	@Mapping(target = "trainee", expression = "java(traineeDAO.findById(trainingRequestDTO.getTraineeId()).get())")
	@Mapping(target = "trainer", expression = "java(trainerDAO.findById(trainingRequestDTO.getTrainerId()).get())")
	public abstract Training trainingRequestDTOToTraining(TrainingRequestDTO trainingRequestDTO);
}
