package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private TraineeRepository traineeRepository;
	@Mock
	private TrainerRepository trainerRepository;
	@Mock
	private TrainingRepository trainingRepository;
	@Mock
	private EntityToModelMapper mapper;
	@Mock
	private UserCredentialsGenerator generator;
	@InjectMocks
	private TraineeServiceImpl traineeService;

	@Test
	void save_shouldAssignGeneratedUserNameAndPassword_whenInvoked() {
		final var request = new TraineeModel();
		final Trainee trainee = new Trainee();
		when(mapper.mapToTrainee(request)).thenReturn(trainee);
		final String userName = "username";
		when(generator.generateUserName(trainee.getUser())).thenReturn(userName);
		when(generator.generatePassword(trainee.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainee.getUser())).thenReturn(trainee.getUser());
		when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
		final TraineeModel expected = new TraineeModel();
		expected.setUserName(userName);
		when(mapper.mapToTraineeModel(trainee)).thenReturn(expected);

		assertEquals(expected, traineeService.save(request));
		verify(mapper, times(1)).mapToTrainee(request);
		verify(generator, times(1)).generateUserName(trainee.getUser());
		verify(generator, times(1)).generatePassword(trainee.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainee.getUser());
		verify(traineeRepository, times(1)).saveAndFlush(trainee);
		verify(mapper, times(1)).mapToTraineeModel(trainee);
	}

	@Test
	void findAll_shouldReturnTwoTraineeModels_whenThereAreTwoTrainees() {
		final List<Trainee> trainees = List.of(new Trainee(), new Trainee());
		when(traineeRepository.findAll()).thenReturn(trainees);
		final List<TraineeModel> response = List.of(new TraineeModel(), new TraineeModel());
		when(mapper.mapToTraineeModelList(trainees)).thenReturn(response);

		final List<TraineeModel> result = traineeService.findAll();

		assertEquals(2, result.size());
		verify(traineeRepository, times(1)).findAll();
		verify(mapper, times(1)).mapToTraineeModelList(trainees);
	}

	@Nested
	class TestFindByUserName {

		@Test
		void findByUserName_shouldReturnTraineeModel_whenTraineeExists() {
			final String userName = "UserName";
			final Trainee trainee = new Trainee();
			trainee.getUser().setUserName(userName);
			when(traineeRepository.findByUserUserName(userName)).thenReturn(java.util.Optional.of(trainee));
			final TraineeModel expectedOutput = new TraineeModel();
			when(mapper.mapToTraineeModel(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.findByUserName(userName));
			verify(traineeRepository, times(1)).findByUserUserName(userName);
			verify(mapper, times(1)).mapToTraineeModel(trainee);
		}

		@Test
		void findByUserName_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final String userName = "UserName";
			when(traineeRepository.findByUserUserName(userName)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.findByUserName(userName));
			verify(traineeRepository, times(1)).findByUserUserName(userName);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldNotUpdateUserNameAndReturnTraineeModel_whenInvokedWithSameNames() {
			final var request = new TraineeModel(
				1L,
				"firstName",
				"lastName",
				"firstName.lastName",
				"Password",
				true,
				1L,
				new Date(),
				"Address",
				null
			);
			final var trainee = new Trainee(
				1L,
				"Fname",
				"Lname",
				"Fname.Lname",
				"password",
				false,
				1L,
				new Date(),
				"addr",
				null
			);
			trainee.setId(request.getId());
			when(traineeRepository.findByUserUserName(request.getUserName())).thenReturn(java.util.Optional.of(trainee));
			when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
			final TraineeModel expectedOutput = new TraineeModel();
			when(mapper.mapToTraineeModel(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.update(request));
			verifyNoInteractions(generator);
			verify(traineeRepository, times(1)).findByUserUserName(request.getUserName());
			verify(traineeRepository, times(1)).saveAndFlush(trainee);
			verify(mapper, times(1)).mapToTraineeModel(trainee);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final TraineeModel request = new TraineeModel();
			final String userName = "User.Name";
			request.setUserName(userName);
			when(traineeRepository.findByUserUserName(request.getUserName())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.update(request));
			verify(traineeRepository, times(1)).findByUserUserName(userName);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestDelete {

		@Test
		void deleteByUserName_shouldRemoveAllTraineeTrainings_whenTraineeHasTrainings() {
			final String userName = "UserName";
			final Trainee trainee = new Trainee();
			trainee.getUser().setUserName(userName);
			final Trainee trainee2 = new Trainee();
			trainee2.getUser().setUserName("AnotherName");
			final Training training1 = new Training(1L, trainee, null, "name", null, null, 30);
			final Training training2 = new Training(2L, trainee, null, "another name", null, null, 60);
			final Training training3 = new Training(3L, trainee2, null, "yet another name", null, null, 45);
			final List<Training> allTrainings = List.of(training1, training2, training3);
			when(traineeRepository.findByUserUserName(userName)).thenReturn(Optional.of(trainee));
			when(trainingRepository.findAll()).thenReturn(allTrainings);
			final List<Training> expectedTrainingsToDelete = List.of(training1, training2);

			traineeService.delete(userName);

			verify(trainingRepository, times(1)).findAll();
			verify(trainingRepository, times(1)).deleteAll(expectedTrainingsToDelete);
			verifyNoMoreInteractions(trainingRepository);
			verify(traineeRepository, times(1)).deleteByUserUserName(userName);
			verifyNoInteractions(mapper);
		}

		@Test
		void deleteByUserName_shouldThrowEntityNotFoundException_whenEntityDoesNotExist() {
			final String userName = "User.Name";
			when(traineeRepository.findByUserUserName(userName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.delete(userName));
		}
	}

	@Nested
	class TestAddTrainer {

		@Test
		void addTrainer_shouldAddTrainerToTrainee_whenBothExist() {
			final String traineeUserName = "Trainee.UserName";
			final String trainerUserName = "Trainer.UserName";
			final var trainee = new Trainee();
			when(trainerRepository.findByUserUserName(trainerUserName)).thenReturn(Optional.of(new Trainer()));
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.of(trainee));

			traineeService.addTrainer(traineeUserName, trainerUserName);
			assertEquals(1, trainee.getTrainers().size());
			verify(trainerRepository, times(1)).findByUserUserName(trainerUserName);
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
		}

		@Test
		void addTrainer_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final String traineeUserName = "Trainee.UserName";
			final String trainerUserName = "Trainer.UserName";
			when(trainerRepository.findByUserUserName(trainerUserName)).thenReturn(Optional.of(new Trainer()));
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.addTrainer(traineeUserName, trainerUserName));
			verify(trainerRepository, times(1)).findByUserUserName(trainerUserName);
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
		}

		@Test
		void addTrainer_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final String traineeUserName = "Trainee.UserName";
			final String trainerUserName = "Trainer.UserName";
			when(trainerRepository.findByUserUserName(trainerUserName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.addTrainer(traineeUserName, trainerUserName));
			verify(trainerRepository, times(1)).findByUserUserName(trainerUserName);
			verifyNoInteractions(traineeRepository);
		}
	}

	@Nested
	class TestUpdateTraineeTrainerList {

		@Test
		void updateTrainers_shouldNotChangeTrainerList_whenTrainerListIsEqualToExisting() {
			final String traineeUserName = "User.Name";
			final var trainee = createTrainee(traineeUserName);
			final String trainerUserName1 = "User.Name2";
			final String trainerUserName2 = "User.Name3";
			final var trainerUserNames = List.of(trainerUserName1, trainerUserName2);
			final var trainer1 = createTrainer(1L, trainerUserName1);
			final var trainer2 = createTrainer(2L, trainerUserName2);
			setTrainers(trainee, trainer1, trainer2);
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.of(trainee));

			traineeService.updateTrainers(traineeUserName, trainerUserNames);

			assertEquals(List.of(trainer1, trainer2), trainee.getTrainers());
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
			verify(traineeRepository, times(1)).saveAndFlush(trainee);
			verifyNoInteractions(trainerRepository);
			verifyNoInteractions(trainingRepository);
		}

		@Test
		void updateTrainers_shouldAddNewTrainerAndRemoveNotMentionedTrainer_whenTrainerListDiffersFromExisting() {
			final String traineeUserName = "User.Name";
			final var trainee = createTrainee(traineeUserName);
			final var trainer1 = createTrainer(1L, "User.Name2");
			setTrainers(trainee, trainer1);
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.of(trainee));
			final var updatedTrainee = createTrainee(traineeUserName);
			final String trainerUserName2 = "User.Name3";
			final var trainer2 = createTrainer(2L, trainerUserName2);
			setTrainers(updatedTrainee, trainer2);
			when(trainerRepository.findByUserUserName(trainerUserName2)).thenReturn(Optional.of(trainer2));
			when(trainingRepository.findAll()).thenReturn(new ArrayList<>());

			traineeService.updateTrainers(traineeUserName, List.of(trainerUserName2));

			assertEquals(List.of(trainer2), trainee.getTrainers());
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
			verify(trainerRepository, times(1)).findByUserUserName(trainerUserName2);
			verify(trainingRepository, times(1)).findAll();
			verify(traineeRepository, times(1)).saveAndFlush(updatedTrainee);
		}

		@Test
		void updateTrainers_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final String traineeUserName = "User.Name";
			final var trainers = List.of("User.Name2");
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.updateTrainers(traineeUserName, trainers));
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
			verifyNoMoreInteractions(traineeRepository);
			verifyNoInteractions(trainerRepository);
			verifyNoInteractions(trainingRepository);
		}

		@Test
		void updateTrainers_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final String traineeUserName = "User.Name";
			final String trainerUserName = "User.Name2";
			final var trainers = List.of(trainerUserName);
			final var trainee = new Trainee();
			when(traineeRepository.findByUserUserName(traineeUserName)).thenReturn(Optional.of(trainee));
			when(trainerRepository.findByUserUserName(trainerUserName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.updateTrainers(traineeUserName, trainers));
			verify(traineeRepository, times(1)).findByUserUserName(traineeUserName);
			verify(trainerRepository, times(1)).findByUserUserName(trainerUserName);
			verifyNoMoreInteractions(traineeRepository);
			verifyNoMoreInteractions(trainerRepository);
			verifyNoInteractions(trainingRepository);
		}


		private Trainee createTrainee(final String userName) {
			final var trainee = new Trainee();
			trainee.setUser(new User());
			trainee.getUser().setUserName(userName);
			return trainee;
		}

		private Trainer createTrainer(final Long id, final String userName) {
			final var trainer = new Trainer();
			trainer.setUser(new User());
			trainer.getUser().setUserName(userName);
			trainer.setId(id);
			return trainer;
		}

		private void setTrainers(final Trainee trainee, final Trainer... trainers) {
			final List<Trainer> trainerList = new ArrayList<>(Arrays.asList(trainers));
			trainee.setTrainers(trainerList);
		}
	}
}
