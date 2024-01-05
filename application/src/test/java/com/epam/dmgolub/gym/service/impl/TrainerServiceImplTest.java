package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
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
	private MapStructMapper mapper;
	@Mock
	private UserCredentialsGenerator userCredentialsGenerator;
	@InjectMocks
	private TrainerServiceImpl trainerService;

	@Test
	void save_shouldAssignGeneratedUserNameAndPassword_whenInvoked() {
		final TrainerRequestDTO request = new TrainerRequestDTO();
		final Trainer trainer = new Trainer();
		when(mapper.trainerRequestDTOToTrainer(request)).thenReturn(trainer);
		final String userName = "username";
		when(userCredentialsGenerator.generateUserName(trainer.getUser())).thenReturn(userName);
		when(userCredentialsGenerator.generatePassword(trainer.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainer.getUser())).thenReturn(trainer.getUser());
		when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
		final TrainerResponseDTO expected = new TrainerResponseDTO();
		expected.setUserName(userName);
		when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(expected);

		assertEquals(expected, trainerService.save(request));
		verify(mapper, times(1)).trainerRequestDTOToTrainer(request);
		verify(userCredentialsGenerator, times(1)).generateUserName(trainer.getUser());
		verify(userCredentialsGenerator, times(1)).generatePassword(trainer.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainer.getUser());
		verify(trainerRepository, times(1)).saveAndFlush(trainer);
		verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainerResponseDTO_whenTrainerExists() {
			final Long id = 1L;
			final Trainer trainer = new Trainer();
			when(trainerRepository.findById(id)).thenReturn(java.util.Optional.of(trainer));
			final TrainerResponseDTO trainerResponseDTO = new TrainerResponseDTO();
			when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(trainerResponseDTO);

			assertEquals(trainerResponseDTO, trainerService.findById(id));
			verify(trainerRepository, times(1)).findById(id);
			verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final Long id = 1L;
			when(trainerRepository.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.findById(id));
			verify(trainerRepository, times(1)).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainerResponseDTOs_whenThereAreTwoTrainers() {
		final List<Trainer> trainers = List.of(new Trainer(), new Trainer());
		when(trainerRepository.findAll()).thenReturn(trainers);
		final List<TrainerResponseDTO> response = List.of(new TrainerResponseDTO(), new TrainerResponseDTO());
		when(mapper.trainerListToTrainerResponseDTOList(trainers)).thenReturn(response);

		assertEquals(response, trainerService.findAll());
		verify(trainerRepository, times(1)).findAll();
		verify(mapper, times(1)).trainerListToTrainerResponseDTOList(trainers);
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldUpdateUserNameAndReturnTrainerResponseDTO_whenInvoked() {
			final TrainerRequestDTO request =
				new TrainerRequestDTO(1L, "firstName", "lastName", true, 1L, null);
			final Trainer trainer =
				new Trainer(1L, "Fname", "Lname", "Fname.Lname", "password", false, 1L, null);
			when(trainerRepository.findById(request.getId())).thenReturn(java.util.Optional.of(trainer));
			final String userName = "username";
			when(userCredentialsGenerator.generateUserName(trainer.getUser())).thenReturn(userName);
			when(trainerRepository.saveAndFlush(trainer)).thenReturn(trainer);
			final TrainerResponseDTO expected = new TrainerResponseDTO();
			when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(expected);

			assertEquals(expected, trainerService.update(request));
			verify(userCredentialsGenerator, times(1)).generateUserName(trainer.getUser());
			verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final TrainerRequestDTO request = new TrainerRequestDTO();
			final Long id = 99L;
			request.setId(id);
			when(trainerRepository.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.update(request));
			verify(trainerRepository, times(1)).findById(id);
		}
	}
}
