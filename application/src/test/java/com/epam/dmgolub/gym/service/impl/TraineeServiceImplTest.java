package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TraineeDAO;
import com.epam.dmgolub.gym.dao.TrainingDAO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import com.epam.dmgolub.gym.service.UserCredentialsGenerator;

import java.util.Date;
import java.util.List;

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
	private TraineeDAO traineeDAO;
	@Mock
	private TrainingDAO trainingDAO;
	@Mock
	private MapStructMapper mapper;
	@Mock
	private UserCredentialsGenerator generator;
	@InjectMocks
	private TraineeServiceImpl traineeService;

	@Test
	void save_shouldAssignGeneratedUserNameAndPassword_whenInvoked() {
		final TraineeRequestDTO request = new TraineeRequestDTO();
		final Trainee trainee = new Trainee();
		when(mapper.traineeRequestDTOToTrainee(request)).thenReturn(trainee);
		final String userName = "username";
		when(generator.generateUserName(trainee)).thenReturn(userName);
		when(generator.generatePassword(trainee)).thenReturn("password");
		when(traineeDAO.save(trainee)).thenReturn(trainee);
		final TraineeResponseDTO expected = new TraineeResponseDTO();
		expected.setUserName(userName);
		when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expected);

		assertEquals(expected, traineeService.save(request));
		verify(mapper, times(1)).traineeRequestDTOToTrainee(request);
		verify(generator, times(1)).generateUserName(trainee);
		verify(generator, times(1)).generatePassword(trainee);
		verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTraineeResponseDTO_whenTraineeExists() {
			final Long id = 1L;
			final Trainee trainee = new Trainee();
			trainee.setId(id);
			when(traineeDAO.findById(id)).thenReturn(java.util.Optional.of(trainee));
			final TraineeResponseDTO expectedOutput = new TraineeResponseDTO();
			when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.findById(id));
			verify(traineeDAO, times(1)).findById(id);
			verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long id = 99L;
			when(traineeDAO.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.findById(id));
			verify(traineeDAO, times(1)).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTraineeResponseDTOs_whenThereAreTwoTrainees() {
		final List<Trainee> trainees = List.of(new Trainee(), new Trainee());
		when(traineeDAO.findAll()).thenReturn(trainees);
		final List<TraineeResponseDTO> response = List.of(new TraineeResponseDTO(), new TraineeResponseDTO());
		when(mapper.traineeListToTraineeResponseDTOList(trainees)).thenReturn(response);

		final List<TraineeResponseDTO> result = traineeService.findAll();

		assertEquals(2, result.size());
		verify(traineeDAO, times(1)).findAll();
		verify(mapper, times(1)).traineeListToTraineeResponseDTOList(trainees);
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldUpdateUserNameAndReturnTraineeResponseDTO_whenInvoked() {
			final TraineeRequestDTO request =
				new TraineeRequestDTO(1L, "firstName", "lastName", true, null, new Date(), "address");
			final Trainee trainee = new Trainee();
			trainee.setId(request.getId());
			when(traineeDAO.findById(request.getId())).thenReturn(java.util.Optional.of(trainee));
			final String userName = "username";
			when(generator.generateUserName(trainee)).thenReturn(userName);
			when(traineeDAO.update(trainee)).thenReturn(trainee);
			final TraineeResponseDTO expectedOutput = new TraineeResponseDTO();
			expectedOutput.setUserName(userName);
			when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.update(request));
			verify(generator, times(1)).generateUserName(trainee);
			verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final TraineeRequestDTO request = new TraineeRequestDTO();
			final Long id = 1L;
			request.setId(id);
			when(traineeDAO.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.update(request));
			verify(traineeDAO, times(1)).findById(id);
		}
	}

	@Test
	void delete_shouldRemoveAllTraineeTrainings_whenTraineeHasTrainings() {
		final Long id = 1L;
		final Trainee trainee = new Trainee();
		trainee.setId(id);
		final Trainee trainee2 = new Trainee();
		trainee2.setId(2L);
		final List<Training> allTrainings = List.of(
			new Training(1L, trainee, null, "name", null, null, 30),
			new Training(2L, trainee, null, "another name", null, null, 60),
			new Training(3L, trainee2, null, "yet another name", null, null, 45)
		);
		when(trainingDAO.findAll()).thenReturn(allTrainings);

		traineeService.delete(id);

		verify(trainingDAO, times(1)).findAll();
		verify(trainingDAO, times(1)).delete(1L);
		verify(trainingDAO, times(1)).delete(2L);
		verifyNoMoreInteractions(trainingDAO);
		verify(traineeDAO, times(1)).delete(id);
	}
}
