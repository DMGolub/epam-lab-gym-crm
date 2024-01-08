package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.mapper.MapStructMapper;
import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
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
	private UserRepository userRepository;
	@Mock
	private TraineeRepository traineeRepository;
	@Mock
	private TrainingRepository trainingRepository;
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
		when(generator.generateUserName(trainee.getUser())).thenReturn(userName);
		when(generator.generatePassword(trainee.getUser())).thenReturn("password");
		when(userRepository.saveAndFlush(trainee.getUser())).thenReturn(trainee.getUser());
		when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
		final TraineeResponseDTO expected = new TraineeResponseDTO();
		expected.setUserName(userName);
		when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expected);

		assertEquals(expected, traineeService.save(request));
		verify(mapper, times(1)).traineeRequestDTOToTrainee(request);
		verify(generator, times(1)).generateUserName(trainee.getUser());
		verify(generator, times(1)).generatePassword(trainee.getUser());
		verify(userRepository, times(1)).saveAndFlush(trainee.getUser());
		verify(traineeRepository, times(1)).saveAndFlush(trainee);
		verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnTraineeResponseDTO_whenTraineeExists() {
			final Long id = 1L;
			final Trainee trainee = new Trainee();
			trainee.setId(id);
			when(traineeRepository.findById(id)).thenReturn(java.util.Optional.of(trainee));
			final TraineeResponseDTO expectedOutput = new TraineeResponseDTO();
			when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.findById(id));
			verify(traineeRepository, times(1)).findById(id);
			verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
		}

		@Test
		void findById_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final Long id = 99L;
			when(traineeRepository.findById(id)).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.findById(id));
			verify(traineeRepository, times(1)).findById(id);
		}
	}

	@Test
	void findAll_shouldReturnTwoTraineeResponseDTOs_whenThereAreTwoTrainees() {
		final List<Trainee> trainees = List.of(new Trainee(), new Trainee());
		when(traineeRepository.findAll()).thenReturn(trainees);
		final List<TraineeResponseDTO> response = List.of(new TraineeResponseDTO(), new TraineeResponseDTO());
		when(mapper.traineeListToTraineeResponseDTOList(trainees)).thenReturn(response);

		final List<TraineeResponseDTO> result = traineeService.findAll();

		assertEquals(2, result.size());
		verify(traineeRepository, times(1)).findAll();
		verify(mapper, times(1)).traineeListToTraineeResponseDTOList(trainees);
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldUpdateUserNameAndReturnTraineeResponseDTO_whenInvoked() {
			final TraineeRequestDTO request =
				new TraineeRequestDTO(1L, "firstName", "lastName", true, null, new Date(), "Address");
			final Trainee trainee =
				new Trainee(1L, "Fname", "Lname", "Fname.Lname", "password", false, 1L, new Date(), "addr", null);
			trainee.setId(request.getId());
			when(traineeRepository.findById(request.getId())).thenReturn(java.util.Optional.of(trainee));
			final String userName = "firstName.lastName";
			when(generator.generateUserName(trainee.getUser())).thenReturn(userName);
			when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
			final TraineeResponseDTO expectedOutput = new TraineeResponseDTO();
			expectedOutput.setUserName(userName);
			when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.update(request));
			verify(generator, times(1)).generateUserName(trainee.getUser());
			verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
		}

		@Test
		void update_shouldNotUpdateUserNameAndReturnTraineeResponseDTO_whenInvokedWithSameNames() {
			final TraineeRequestDTO request =
				new TraineeRequestDTO(1L, "firstName", "lastName", true, null, new Date(), "Address");
			final Trainee trainee =
				new Trainee(1L, "firstName", "lastName", "firstName.lastName", "password", false, 1L, new Date(), "addr", null);
			trainee.setId(request.getId());
			when(traineeRepository.findById(request.getId())).thenReturn(java.util.Optional.of(trainee));
			when(traineeRepository.saveAndFlush(trainee)).thenReturn(trainee);
			final TraineeResponseDTO expectedOutput = new TraineeResponseDTO();
			when(mapper.traineeToTraineeResponseDTO(trainee)).thenReturn(expectedOutput);

			assertEquals(expectedOutput, traineeService.update(request));
			verifyNoInteractions(generator);
			verify(mapper, times(1)).traineeToTraineeResponseDTO(trainee);
		}

		@Test
		void update_shouldThrowEntityNotFoundException_whenTraineeNotFound() {
			final TraineeRequestDTO request = new TraineeRequestDTO();
			final Long id = 1L;
			request.setId(id);
			when(traineeRepository.findById(request.getId())).thenReturn(java.util.Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> traineeService.update(request));
			verify(traineeRepository, times(1)).findById(id);
		}
	}

	@Test
	void delete_shouldRemoveAllTraineeTrainings_whenTraineeHasTrainings() {
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
	}
}
