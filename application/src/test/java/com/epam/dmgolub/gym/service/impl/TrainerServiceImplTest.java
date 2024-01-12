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
		when(mapper.trainerModelToTrainer(request)).thenReturn(trainer);
		final String userName = "username";
		when(generator.generateUserName(trainer.getUser())).thenReturn(userName);
		when(generator.generatePassword(trainer.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainer.getUser())).thenReturn(trainer.getUser());
		when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
		final TrainerModel expected = new TrainerModel();
		expected.setUserName(userName);
		when(mapper.trainerToTrainerModel(trainer)).thenReturn(expected);

		assertEquals(expected, trainerService.save(request));
		verify(mapper, times(1)).trainerModelToTrainer(request);
		verify(generator, times(1)).generateUserName(trainer.getUser());
		verify(generator, times(1)).generatePassword(trainer.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainer.getUser());
		verify(trainerRepository, times(1)).saveAndFlush(trainer);
		verify(mapper, times(1)).trainerToTrainerModel(trainer);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainerModel_whenTrainerExists() {
			final Long id = 1L;
			final Trainer trainer = new Trainer();
			when(trainerRepository.findById(id)).thenReturn(java.util.Optional.of(trainer));
			final TrainerModel trainerModel = new TrainerModel();
			when(mapper.trainerToTrainerModel(trainer)).thenReturn(trainerModel);

			assertEquals(trainerModel, trainerService.findById(id));
			verify(trainerRepository, times(1)).findById(id);
			verify(mapper, times(1)).trainerToTrainerModel(trainer);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final Long id = 1L;
			when(trainerRepository.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.findById(id));
			verify(trainerRepository, times(1)).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainerModels_whenThereAreTwoTrainers() {
		final List<Trainer> trainers = List.of(new Trainer(), new Trainer());
		when(trainerRepository.findAll()).thenReturn(trainers);
		final List<TrainerModel> response = List.of(new TrainerModel(), new TrainerModel());
		when(mapper.trainerListToTrainerModelList(trainers)).thenReturn(response);

		assertEquals(response, trainerService.findAll());
		verify(trainerRepository, times(1)).findAll();
		verify(mapper, times(1)).trainerListToTrainerModelList(trainers);
	}

	@Nested
	class TestFindByUserName {

		@Test
		void findByUserName_shouldReturnTrainerModel_whenTrainerExists() {
			final String userName = "UserName";
			final Trainer trainer = new Trainer();
			when(trainerRepository.findByUserUserName(userName)).thenReturn(java.util.Optional.of(trainer));
			final TrainerModel trainerModel = new TrainerModel();
			when(mapper.trainerToTrainerModel(trainer)).thenReturn(trainerModel);

			assertEquals(trainerModel, trainerService.findByUserName(userName));
			verify(trainerRepository, times(1)).findByUserUserName(userName);
			verify(mapper, times(1)).trainerToTrainerModel(trainer);
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
			when(trainerRepository.findById(request.getId())).thenReturn(java.util.Optional.of(trainer));
			when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
			final TrainerModel expected = new TrainerModel();
			when(mapper.trainerToTrainerModel(trainer)).thenReturn(expected);

			assertEquals(expected, trainerService.update(request));
			verifyNoInteractions(generator);
			verify(mapper, times(1)).trainerToTrainerModel(trainer);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final TrainerModel request = new TrainerModel();
			final Long id = 99L;
			request.setId(id);
			when(trainerRepository.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.update(request));
			verify(trainerRepository, times(1)).findById(id);
			verifyNoInteractions(mapper);
		}
	}

	@Nested
	class TestFindActiveTrainersAssignedToTrainee {

		@Test
		void findActiveTrainersAssignedToTrainee_shouldReturnTrainerModelList_whenThereAreActiveTrainers() {
			final Long traineeId = 1L;
			final var trainers = List.of(new Trainer(), new Trainer());
			when(trainerRepository.findActiveTrainersAssignedToTrainee(traineeId)).thenReturn(trainers);
			final var expected = List.of(new TrainerModel(), new TrainerModel());
			when(mapper.trainerListToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersAssignedToTrainee(traineeId);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersAssignedToTrainee(traineeId);
			verify(mapper, times(1)).trainerListToTrainerModelList(trainers);
		}

		@Test
		void findActiveTrainersAssignedToTrainee_shouldReturnEmptyList_whenThereAreNoTrainers() {
			final Long traineeId = 1L;
			final var trainers = new ArrayList<Trainer>();
			when(trainerRepository.findActiveTrainersAssignedToTrainee(traineeId)).thenReturn(trainers);
			final var expected = new ArrayList<TrainerModel>();
			when(mapper.trainerListToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersAssignedToTrainee(traineeId);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersAssignedToTrainee(traineeId);
			verify(mapper, times(1)).trainerListToTrainerModelList(trainers);
		}
	}

	@Nested
	class TestFindActiveTrainersNotAssignedToTrainee {

		@Test
		void findActiveTrainersNotAssignedToTrainee_shouldReturnTrainerModelList_whenThereAreActiveTrainers() {
			final Long traineeId = 1L;
			final var trainers = List.of(new Trainer(), new Trainer());
			when(trainerRepository.findActiveTrainersNotAssignedToTrainee(traineeId)).thenReturn(trainers);
			final var expected = List.of(new TrainerModel(), new TrainerModel());
			when(mapper.trainerListToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersNotAssignedToTrainee(traineeId);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersNotAssignedToTrainee(traineeId);
			verify(mapper, times(1)).trainerListToTrainerModelList(trainers);
		}

		@Test
		void findActiveTrainersNotAssignedToTrainee_shouldReturnEmptyList_whenThereAreNoTrainers() {
			final Long traineeId = 1L;
			final var trainers = new ArrayList<Trainer>();
			when(trainerRepository.findActiveTrainersNotAssignedToTrainee(traineeId)).thenReturn(trainers);
			final var expected = new ArrayList<TrainerModel>();
			when(mapper.trainerListToTrainerModelList(trainers)).thenReturn(expected);

			final var result = trainerService.findActiveTrainersNotAssignedToTrainee(traineeId);

			assertEquals(expected, result);
			verify(trainerRepository, times(1)).findActiveTrainersNotAssignedToTrainee(traineeId);
			verify(mapper, times(1)).trainerListToTrainerModelList(trainers);
		}
	}
}
