package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TrainerDAO;
import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
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
	private TrainerDAO trainerDAO;
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
		when(userCredentialsGenerator.generateUserName(trainer)).thenReturn(userName);
		when(userCredentialsGenerator.generatePassword(trainer)).thenReturn("password");
		when(trainerDAO.save(trainer)).thenReturn(trainer);
		final TrainerResponseDTO expected = new TrainerResponseDTO();
		expected.setUserName(userName);
		when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(expected);

		assertEquals(expected, trainerService.save(request));
		verify(mapper, times(1)).trainerRequestDTOToTrainer(request);
		verify(userCredentialsGenerator, times(1)).generateUserName(trainer);
		verify(userCredentialsGenerator, times(1)).generatePassword(trainer);
		verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTrainerResponseDTO_whenTrainerExists() {
			final Long id = 1L;
			final Trainer trainer = new Trainer();
			when(trainerDAO.findById(id)).thenReturn(java.util.Optional.of(trainer));
			final TrainerResponseDTO trainerResponseDTO = new TrainerResponseDTO();
			when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(trainerResponseDTO);

			assertEquals(trainerResponseDTO, trainerService.findById(id));
			verify(trainerDAO, times(1)).findById(id);
			verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final Long id = 1L;
			when(trainerDAO.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.findById(id));
			verify(trainerDAO, times(1)).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTrainerResponseDTOs_whenThereAreTwoTrainers() {
		final List<Trainer> trainers = List.of(new Trainer(), new Trainer());
		when(trainerDAO.findAll()).thenReturn(trainers);
		final List<TrainerResponseDTO> response = List.of(new TrainerResponseDTO(), new TrainerResponseDTO());
		when(mapper.trainerListToTrainerResponseDTOList(trainers)).thenReturn(response);

		assertEquals(response, trainerService.findAll());
		verify(trainerDAO, times(1)).findAll();
		verify(mapper, times(1)).trainerListToTrainerResponseDTOList(trainers);
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldUpdateUserNameAndReturnTrainerResponseDTO_whenInvoked() {
			final TrainerRequestDTO request = new TrainerRequestDTO();
			request.setId(1L);
			final Trainer trainer = new Trainer();
			when(trainerDAO.findById(request.getId())).thenReturn(java.util.Optional.of(trainer));
			final String userName = "username";
			when(userCredentialsGenerator.generateUserName(trainer)).thenReturn(userName);
			when(trainerDAO.update(trainer)).thenReturn(trainer);
			final TrainerResponseDTO expected = new TrainerResponseDTO();
			when(mapper.trainerToTrainerResponseDTO(trainer)).thenReturn(expected);

			assertEquals(expected, trainerService.update(request));
			verify(userCredentialsGenerator, times(1)).generateUserName(trainer);
			verify(mapper, times(1)).trainerToTrainerResponseDTO(trainer);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTrainerNotFound() {
			final TrainerRequestDTO request = new TrainerRequestDTO();
			final Long id = 99L;
			request.setId(id);
			when(trainerDAO.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> trainerService.update(request));
			verify(trainerDAO, times(1)).findById(id);
		}
	}
}
