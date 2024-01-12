package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.repository.searchcriteria.TraineeTrainingsSearchCriteria;
import com.epam.dmgolub.gym.repository.searchcriteria.TrainerTrainingsSearchCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EntityToModelMapper {

	protected TraineeRepository traineeRepository;
	protected TrainerRepository trainerRepository;
	protected TrainingTypeRepository trainingTypeRepository;

	@Autowired
	public void setTraineeRepository(final TraineeRepository traineeRepository) {
		this.traineeRepository = traineeRepository;
	}

	@Autowired
	public void setTrainerRepository(final TrainerRepository trainerRepository) {
		this.trainerRepository = trainerRepository;
	}

	@Autowired
	public void setTrainingTypeRepository(final TrainingTypeRepository trainingTypeRepository) {
		this.trainingTypeRepository = trainingTypeRepository;
	}

	@Mapping(target = "userId", source = "trainee.user.id")
	@Mapping(target = "firstName", source = "trainee.user.firstName")
	@Mapping(target = "lastName", source = "trainee.user.lastName")
	@Mapping(target = "userName", source = "trainee.user.userName")
	@Mapping(target = "active", source = "trainee.user.active")
	public abstract TraineeModel traineeToTraineeModel(Trainee trainee);

	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	public abstract TraineeModel.Trainer trainerToTraineeModelTrainer(Trainer trainer);

	public abstract List<TraineeModel> traineeListToTraineeModelList(List<Trainee> trainees);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainers", ignore = true)
	public abstract Trainee traineeModelToTrainee(TraineeModel traineeModel);

	public abstract TrainingTypeModel trainingTypeToTrainingTypeModel(TrainingType trainingType);

	public abstract List<TrainingTypeModel> trainingTypeListToTrainingTypeModelList(List<TrainingType> trainingTypes);

	public abstract TrainingType trainingTypeModelToTrainingType(TrainingTypeModel trainingTypeModel);

	@Mapping(target = "userId", source = "trainer.user.id")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "active", source = "trainer.user.active")
	public abstract TrainerModel trainerToTrainerModel(Trainer trainer);

	public abstract List<TrainerModel> trainerListToTrainerModelList(List<Trainer> trainers);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainees", ignore = true)
	public abstract Trainer trainerModelToTrainer(TrainerModel trainerModel);

	public abstract TrainingModel trainingToTrainingModel(Training training);

	public abstract List<TrainingModel> trainingListToTrainingModelList(List<Training> trainings);

	@Mapping(target = "trainee", expression = "java(traineeRepository.findById(trainingModel.getTrainee().getId()).get())")
	@Mapping(target = "trainer", expression = "java(trainerRepository.findById(trainingModel.getTrainer().getId()).get())")
	@Mapping(target = "type", expression = "java(trainingTypeRepository.findById(" +
		"trainerRepository.findById(trainingModel.getTrainer().getId()).get().getSpecialization().getId()).get())")
	public abstract Training trainingModelToTraining(TrainingModel trainingModel);

	public abstract TraineeTrainingsSearchCriteria searchRequestToSearchCriteria(TraineeTrainingsSearchRequest request);

	public abstract TrainerTrainingsSearchCriteria searchRequestToSearchCriteria(TrainerTrainingsSearchRequest request);
}
