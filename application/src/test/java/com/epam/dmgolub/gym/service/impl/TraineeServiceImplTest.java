package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;

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
		when(mapper.traineeModelToTrainee(request)).thenReturn(trainee);
		final String userName = "username";
		when(generator.generateUserName(trainee.getUser())).thenReturn(userName);
		when(generator.generatePassword(trainee.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainee.getUser())).thenReturn(trainee.getUser());
		when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
		final TraineeModel expected = new TraineeModel();
		expected.setUserName(userName);
		when(mapper.traineeToTraineeModel(trainee)).thenReturn(expected);

		assertEquals(expected, traineeService.save(request));
		verify(mapper, times(1)).traineeModelToTrainee(request);
		verify(generator, times(1)).generateUserName(trainee.getUser());
		verify(generator, times(1)).generatePassword(trainee.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainee.getUser());
		verify(traineeRepository, times(1)).saveAndFlush(trainee);
		verify(mapper, times(1)).traineeToTraineeModel(trainee);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTraineeModel_whenTraineeExists() {
			final Long id = 1L;
			final Trainee trainee = new Trainee();
			trainee.setId(id);
			when(traineeRepository.findById(id)).thenReturn(java.util.Optional.of(trainee));
			final TraineeModel expectedOutput = new TraineeModel();
			when(mapper.traineeToTraineeModel(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.findById(id));
			verify(traineeRepository, times(1)).findById(id);
			verify(mapper, times(1)).traineeToTraineeModel(trainee);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long id = 99L;
			when(traineeRepository.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.findById(id));
			verify(traineeRepository, times(1)).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Test
	void findAll_shouldReturnTwoTraineeModels_whenThereAreTwoTrainees() {
		final List<Trainee> trainees = List.of(new Trainee(), new Trainee());
		when(traineeRepository.findAll()).thenReturn(trainees);
		final List<TraineeModel> response = List.of(new TraineeModel(), new TraineeModel());
		when(mapper.traineeListToTraineeModelList(trainees)).thenReturn(response);

		final List<TraineeModel> result = traineeService.findAll();

		assertEquals(2, result.size());
		verify(traineeRepository, times(1)).findAll();
		verify(mapper, times(1)).traineeListToTraineeModelList(trainees);
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
			when(mapper.traineeToTraineeModel(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.findByUserName(userName));
			verify(traineeRepository, times(1)).findByUserUserName(userName);
			verify(mapper, times(1)).traineeToTraineeModel(trainee);
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
			when(traineeRepository.findById(request.getId())).thenReturn(java.util.Optional.of(trainee));
			when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
			final TraineeModel expectedOutput = new TraineeModel();
			when(mapper.traineeToTraineeModel(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.update(request));
			verifyNoInteractions(generator);
			verify(mapper, times(1)).traineeToTraineeModel(trainee);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final TraineeModel request = new TraineeModel();
			final Long id = 1L;
			request.setId(id);
			when(traineeRepository.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.update(request));
			verify(traineeRepository, times(1)).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestDelete {

		@Test
		void deleteById_shouldRemoveAllTraineeTrainings_whenTraineeHasTrainings() {
			final Long id = 1L;
			final Trainee trainee = new Trainee();
			trainee.setId(id);
			final Trainee trainee2 = new Trainee();
			trainee2.setId(2L);
			final Training training1 = new Training(1L, trainee, null, "name", null, null, 30);
			final Training training2 = new Training(2L, trainee, null, "another name", null, null, 60);
			final Training training3 = new Training(3L, trainee2, null, "yet another name", null, null, 45);
			final List<Training> allTrainings = List.of(training1, training2, training3);
			when(trainingRepository.findAll()).thenReturn(allTrainings);
			final List<Training> expectedTrainingsToDelete = List.of(training1, training2);

			traineeService.delete(id);

			verify(trainingRepository, times(1)).findAll();
			verify(trainingRepository, times(1)).deleteAll(expectedTrainingsToDelete);
			verifyNoMoreInteractions(trainingRepository);
			verify(traineeRepository, times(1)).deleteById(id);
			verifyNoInteractions(mapper);
		}

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
			when(trainingRepository.findAll()).thenReturn(allTrainings);
			final List<Training> expectedTrainingsToDelete = List.of(training1, training2);

			traineeService.delete(userName);

			verify(trainingRepository, times(1)).findAll();
			verify(trainingRepository, times(1)).deleteAll(expectedTrainingsToDelete);
			verifyNoMoreInteractions(trainingRepository);
			verify(traineeRepository, times(1)).deleteByUserUserName(userName);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestAddTrainer {

		@Test
		void addTrainer_shouldAddTrainerToTrainee_whenBothExist() {
			final Long traineeId = 99L;
			final Long trainerId = 99L;
			final var trainee = new Trainee();
			when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(new Trainer()));
			when(traineeRepository.findById(trainerId)).thenReturn(Optional.of(trainee));

			traineeService.addTrainer(traineeId, trainerId);
			assertEquals(1, trainee.getTrainers().size());
			verify(trainerRepository, times(1)).findById(trainerId);
			verify(traineeRepository, times(1)).findById(traineeId);
		}

		@Test
		void addTrainer_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long traineeId = 99L;
			final Long trainerId = 99L;
			when(trainerRepository.findById(trainerId)).thenReturn(Optional.of(new Trainer()));
			when(traineeRepository.findById(traineeId)).thenThrow(new EntityNotFoundException("Not found"));

			assertThrows(EntityNotFoundException.class, () -> traineeService.addTrainer(traineeId, trainerId));
			verify(trainerRepository, times(1)).findById(trainerId);
			verify(traineeRepository, times(1)).findById(traineeId);
		}

		@Test
		void addTrainer_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final Long traineeId = 99L;
			final Long trainerId = 99L;
			when(trainerRepository.findById(trainerId)).thenThrow(new EntityNotFoundException("Not found"));

			assertThrows(EntityNotFoundException.class, () -> traineeService.addTrainer(traineeId, trainerId));
			verify(trainerRepository, times(1)).findById(trainerId);
			verifyNoInteractions(traineeRepository);
		}
	}
}
