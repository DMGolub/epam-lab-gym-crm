package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private TrainerRepository trainerRepository;
	@Mock
	private EntityToModelMapper mapper;
	@Mock
	private UserCredentialsGenerator generator;
	@InjectMocks
	private TrainerServiceImpl trainerService;

	@Test
	void save_shouldAssignGeneratedUserNameAndPassword_whenInvoked() {
		final TrainerModel request = new TrainerModel();
		final Trainer trainer = new Trainer();
		when(mapper.mapToTrainer(request)).thenReturn(trainer);
		final String userName = "username";
		when(generator.generateUserName(trainer.getUser())).thenReturn(userName);
		when(generator.generatePassword(trainer.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainer.getUser())).thenReturn(trainer.getUser());
		when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
		final TrainerModel expected = new TrainerModel();
		expected.setUserName(userName);
		when(mapper.mapToTrainerModel(trainer)).thenReturn(expected);

		assertEquals(expected, trainerService.save(request));
		verify(mapper, times(1)).mapToTrainer(request);
		verify(generator, times(1)).generateUserName(trainer.getUser());
		verify(generator, times(1)).generatePassword(trainer.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainer.getUser());
		verify(trainerRepository, times(1)).saveAndFlush(trainer);
		verify(mapper, times(1)).mapToTrainerModel(trainer);
	}

	@Test
	void findAll_shouldReturnTwoTrainerModels_whenThereAreTwoTrainers() {
		final List<Trainer> trainers = List.of(new Trainer(), new Trainer());
		when(trainerRepository.findAll()).thenReturn(trainers);
		final List<TrainerModel> response = List.of(new TrainerModel(), new TrainerModel());
		when(mapper.mapToTrainerModelList(trainers)).thenReturn(response);

		assertEquals(response, trainerService.findAll());
		verify(trainerRepository, times(1)).findAll();
		verify(mapper, times(1)).mapToTrainerModelList(trainers);
	}

	@Nested
	class TestFindByUserName {

		@Test
		void findByUserName_shouldReturnTrainerModel_whenTrainerExists() {
			final String userName = "UserName";
			final Trainer trainer = new Trainer();
			when(trainerRepository.findByUserUserName(userName)).thenReturn(java.util.Optional.of(trainer));
			final TrainerModel trainerModel = new TrainerModel();
			when(mapper.mapToTrainerModel(trainer)).thenReturn(trainerModel);

			assertEquals(trainerModel, trainerService.findByUserName(userName));
			verify(trainerRepository, times(1)).findByUserUserName(userName);
			verify(mapper, times(1)).mapToTrainerModel(trainer);
		}

		@Test
		void findByUserName_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final String userName = "UserName";
			when(trainerRepository.findByUserUserName(userName)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.findByUserName(userName));
			verify(trainerRepository, times(1)).findByUserUserName(userName);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldNotUpdateUserNameAndReturnTrainerModel_whenInvokedWithSameNames() {
			final TrainerModel request = new TrainerModel(
				1L,
				"firstName",
				"lastName",
				"firstName.lastName",
				"password",
				true,
				1L,
				null
			);
			final Trainer trainer = new Trainer(
				1L,
				"firstName",
				"lastName",
				"firstName.lastName",
				"password",
				false,
				1L,
				null,
				null
			);
			when(trainerRepository.findByUserUserName(request.getUserName())).thenReturn(java.util.Optional.of(trainer));
			when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
			final TrainerModel expected = new TrainerModel();
			when(mapper.mapToTrainerModel(trainer)).thenReturn(expected);

			assertEquals(expected, trainerService.update(request));
			verifyNoInteractions(generator);
			verify(trainerRepository, times(1)).findByUserUserName(request.getUserName());
			verify(trainerRepository, times(1)).saveAndFlush(trainer);
			verify(mapper, times(1)).mapToTrainerModel(trainer);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final TrainerModel request = new TrainerModel();
			final String userName = "User.Name";
			request.setUserName(userName);
			when(trainerRepository.findByUserUserName(request.getUserName())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.update(request));
			verify(trainerRepository, times(1)).findByUserUserName(userName);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestFindActiveTrainersAssignedToTrainee {

		@Test
		void findActiveTrainersAssignedToTraineeByUserName_shouldReturnTrainerModelList_whenThereAreActiveTrainers() {
			final String traineeUserName = "User.Name";
			final var trainers = List.of(new Trainer(), new Trainer());
			when(trainerRepository.findActiveTrainersAssignedOnTrainee(traineeUserName)).thenReturn(trainers);
			final var expected = List.of(new TrainerModel(), new TrainerModel());
			when(mapper.mapToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersAssignedOnTrainee(traineeUserName);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersAssignedOnTrainee(traineeUserName);
			verify(mapper, times(1)).mapToTrainerModelList(trainers);
		}

		@Test
		void findActiveTrainersAssignedToTraineeByUserName_shouldReturnEmptyList_whenThereAreNoTrainers() {
			final String traineeUserName = "User.Name";
			final var trainers = new ArrayList<Trainer>();
			when(trainerRepository.findActiveTrainersAssignedOnTrainee(traineeUserName)).thenReturn(trainers);
			final var expected = new ArrayList<TrainerModel>();
			when(mapper.mapToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersAssignedOnTrainee(traineeUserName);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersAssignedOnTrainee(traineeUserName);
			verify(mapper, times(1)).mapToTrainerModelList(trainers);
		}
	}

	@Nested
	class TestFindActiveTrainersNotAssignedToTrainee {

		@Test
		void findActiveTrainersNotAssignedToTraineeByUserName_shouldReturnTrainerModelList_whenThereAreActiveTrainers() {
			final String traineeUserName = "User.Name";
			final var trainers = List.of(new Trainer(), new Trainer());
			when(trainerRepository.findActiveTrainersNotAssignedOnTrainee(traineeUserName)).thenReturn(trainers);
			final var expected = List.of(new TrainerModel(), new TrainerModel());
			when(mapper.mapToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersNotAssignedOnTrainee(traineeUserName);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersNotAssignedOnTrainee(traineeUserName);
			verify(mapper, times(1)).mapToTrainerModelList(trainers);
		}

		@Test
		void findActiveTrainersNotAssignedToTraineeByUserName_shouldReturnEmptyList_whenThereAreNoTrainers() {
			final String traineeUserName = "User.Name";
			final var trainers = new ArrayList<Trainer>();
			when(trainerRepository.findActiveTrainersNotAssignedOnTrainee(traineeUserName)).thenReturn(trainers);
			final var expected = new ArrayList<TrainerModel>();
			when(mapper.mapToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersNotAssignedOnTrainee(traineeUserName);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersNotAssignedOnTrainee(traineeUserName);
			verify(mapper, times(1)).mapToTrainerModelList(trainers);
		}
	}
}
