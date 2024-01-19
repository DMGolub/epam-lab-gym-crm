package com.epam.dmgolub.gym.mapper;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TraineeTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainerTrainingsSearchRequest;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.model.UserModel;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.repository.searchcriteria.TraineeTrainingsSearchCriteria;
import com.epam.dmgolub.gym.repository.searchcriteria.TrainerTrainingsSearchCriteria;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
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
	@Mapping(target = "password", source = "trainee.user.password")
	@Mapping(target = "active", source = "trainee.user.active")
	public abstract TraineeModel mapToTraineeModel(Trainee trainee);

	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	public abstract TraineeModel.Trainer mapToTraineeModelTrainer(Trainer trainer);

	public abstract List<TraineeModel> mapToTraineeModelList(List<Trainee> trainees);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainers", ignore = true)
	public abstract Trainee mapToTrainee(TraineeModel traineeModel);

	public abstract TrainingTypeModel mapToTrainingTypeModel(TrainingType trainingType);

	public abstract List<TrainingTypeModel> mapToTrainingTypeModelList(List<TrainingType> trainingTypes);

	public TrainingType mapToTrainingType(final TrainingTypeModel trainingTypeModel) {
		if (trainingTypeModel == null) {
			return null;
		}
		final Long id = trainingTypeModel.getId();
		return trainingTypeRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Can not find training type by id=" + id));
	}

	@Mapping(target = "userId", source = "trainer.user.id")
	@Mapping(target = "firstName", source = "trainer.user.firstName")
	@Mapping(target = "lastName", source = "trainer.user.lastName")
	@Mapping(target = "userName", source = "trainer.user.userName")
	@Mapping(target = "password", source = "trainer.user.password")
	@Mapping(target = "active", source = "trainer.user.active")
	public abstract TrainerModel mapToTrainerModel(Trainer trainer);

	@Mapping(target = "userName", source = "trainee.user.userName")
	@Mapping(target = "firstName", source = "trainee.user.firstName")
	@Mapping(target = "lastName", source = "trainee.user.lastName")
	public abstract TrainerModel.Trainee mapToTrainerModelTrainee(Trainee trainee);

	public abstract List<TrainerModel> mapToTrainerModelList(List<Trainer> trainers);

	@Mapping(source = "userId", target = "user.id")
	@Mapping(source = "firstName", target = "user.firstName")
	@Mapping(source = "lastName", target = "user.lastName")
	@Mapping(source = "active", target = "user.active")
	@Mapping(target = "trainees", ignore = true)
	public abstract Trainer mapToTrainer(TrainerModel trainerModel);

	public abstract TrainingModel mapToTrainingModel(Training training);

	public abstract List<TrainingModel> mapToTrainingModelList(List<Training> trainings);

	@Mapping(target = "trainee", expression = "java(traineeRepository.findByUserUserName(trainingModel.getTrainee().getUserName()).get())")
	@Mapping(target = "trainer", expression = "java(trainerRepository.findByUserUserName(trainingModel.getTrainer().getUserName()).get())")
	@Mapping(target = "type", expression = "java(trainingTypeRepository.findById(" +
		"trainerRepository.findByUserUserName(trainingModel.getTrainer().getUserName()).get().getSpecialization().getId()).get())")
	public abstract Training mapToTraining(TrainingModel trainingModel);

	public abstract TraineeTrainingsSearchCriteria mapToTraineeTrainingsSearchCriteria(TraineeTrainingsSearchRequest request);

	public abstract TrainerTrainingsSearchCriteria mapToTrainerTrainingsSearchCriteria(TrainerTrainingsSearchRequest request);

	public abstract UserModel mapToUserModel(User user);

	public abstract List<UserModel> mapToUserModelList(List<User> users);
}
