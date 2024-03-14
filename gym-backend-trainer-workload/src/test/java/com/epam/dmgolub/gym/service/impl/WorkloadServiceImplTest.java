package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.TrainerWorkload;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import com.epam.dmgolub.gym.repository.WorkloadRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadServiceImplTest {

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final String userName = "FirstName.LastName";
	private final String firstName = "FirstName";
	private final String lastName = "LastName";
	private final boolean isActive = true;
	@Mock
	private WorkloadRepository workloadRepository;
	@InjectMocks
	private WorkloadServiceImpl workloadService;

	@Nested
	class TestAddTraining {

		@Test
		void add_shouldSaveNewWorkload_whenTrainerWorkloadDoesNotExist() throws Exception {
			final var date = dateFormat.parse("2024-04-05");
			final var duration = 90;
			final var training = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(null);
			final var expectedYears =
				List.of(new TrainerWorkload.Year(2024, List.of(new TrainerWorkload.Month(4, duration))));
			final var expected = new TrainerWorkload(userName, firstName, lastName, isActive, expectedYears);

			workloadService.addTraining(training);

			verify(workloadRepository).findByTrainerUserName(userName);
			verify(workloadRepository).saveOfUpdate(expected);
		}

		@Test
		void add_shouldAddWorkload_whenExistingTrainerWorkloadDoesNotContainYear() throws Exception {
			final var date = dateFormat.parse("2024-04-05");
			final var duration = 90;
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingYear = new TrainerWorkload.Year(2023, List.of(new TrainerWorkload.Month(3, 100)));
			final var existingYears = new ArrayList<TrainerWorkload.Year>();
			existingYears.add(existingYear);
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);
			final var expectedYears =
				List.of(existingYear, new TrainerWorkload.Year(2024, List.of(new TrainerWorkload.Month(4, duration))));
			final var expected = new TrainerWorkload(userName, firstName, lastName, isActive, expectedYears);

			workloadService.addTraining(request);

			verify(workloadRepository).findByTrainerUserName(userName);
			verify(workloadRepository).saveOfUpdate(expected);
		}

		@Test
		void add_shouldAddWorkload_whenExistingTrainerWorkloadDoesNotContainMonth() throws Exception {
			final var date = dateFormat.parse("2024-04-05");
			final var duration = 90;
			final var training = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingMonth = new TrainerWorkload.Month(3, 100);
			final var existingMonths = new ArrayList<TrainerWorkload.Month>();
			existingMonths.add(existingMonth);
			final var existingYears = List.of(new TrainerWorkload.Year(2024, existingMonths));
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);
			final var expectedYears =
				List.of(new TrainerWorkload.Year(2024, List.of(existingMonth, new TrainerWorkload.Month(4, 90))));
			final var expected = new TrainerWorkload(userName, firstName, lastName, isActive, expectedYears);

			workloadService.addTraining(training);

			verify(workloadRepository).findByTrainerUserName(userName);
			verify(workloadRepository).saveOfUpdate(expected);
		}

		@Test
		void add_shouldAddWorkload_whenExistingTrainerWorkloadContainsMonth() throws Exception {
			final var date = dateFormat.parse("2024-04-05");
			final var duration = 90;
			final var training = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingYears =
				List.of(new TrainerWorkload.Year(2024, List.of(new TrainerWorkload.Month(4, 100))));
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);
			final var expectedYears =
				List.of(new TrainerWorkload.Year(2024, List.of(new TrainerWorkload.Month(4, 190))));
			final var expected = new TrainerWorkload(userName, firstName, lastName, isActive, expectedYears);

			workloadService.addTraining(training);

			verify(workloadRepository).findByTrainerUserName(userName);
			verify(workloadRepository).saveOfUpdate(expected);
		}
	}

	@Nested
	class TestDeleteTraining {

		@Test
		void delete_shouldNotSubtractDuration_whenTrainingDateIsInThePast() throws Exception {
			final var date = dateFormat.parse("2000-04-05");
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, 90);

			final boolean result = workloadService.deleteTraining(request);

			assertFalse(result);
			verifyNoInteractions(workloadRepository);
		}

		@Test
		void delete_shouldNotModifyWorkload_whenTrainerWorkloadNotFoundByUserName() throws Exception {
			final var date = dateFormat.parse("2030-04-05");
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, 90);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(null);

			final boolean result = workloadService.deleteTraining(request);

			assertFalse(result);
			verify(workloadRepository).findByTrainerUserName(userName);
			verifyNoMoreInteractions(workloadRepository);
		}

		@Test
		void delete_shouldNotModifyWorkload_whenExistingWorkloadDoesNotContainRequestedYear() throws Exception {
			final var date = dateFormat.parse("2030-04-05");
			final var duration = 90;
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, new ArrayList<>());
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);

			final boolean result = workloadService.deleteTraining(request);

			assertFalse(result);
			verify(workloadRepository).findByTrainerUserName(userName);
			verifyNoMoreInteractions(workloadRepository);
		}

		@Test
		void delete_shouldNotModifyWorkload_whenExistingWorkloadDoesNotContainRequestedMonth() throws Exception {
			final var date = dateFormat.parse("2030-04-05");
			final var duration = 90;
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingYears = List.of(new TrainerWorkload.Year(2030, new ArrayList<>()));
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);

			final boolean result = workloadService.deleteTraining(request);

			assertFalse(result);
			verify(workloadRepository).findByTrainerUserName(userName);
			verifyNoMoreInteractions(workloadRepository);
		}

		@Test
		void delete_shouldNotModifyWorkload_whenExistingWorkloadIsNotEnough() throws Exception {
			final var date = dateFormat.parse("2030-04-05");
			final var duration = 90;
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingYears =
				List.of(new TrainerWorkload.Year(2030, List.of(new TrainerWorkload.Month(4, 30))));
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);

			final boolean result = workloadService.deleteTraining(request);

			assertFalse(result);
			verify(workloadRepository).findByTrainerUserName(userName);
			verifyNoMoreInteractions(workloadRepository);
		}

		@Test
		void delete_shouldSubtractDuration_whenExistingWorkloadIsEnough() throws Exception {
			final var date = dateFormat.parse("2030-04-05");
			final var duration = 90;
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			final var existingMonths = List.of(new TrainerWorkload.Month(4, 200));
			final var existingYears = List.of(new TrainerWorkload.Year(2030, existingMonths));
			final var workload = new TrainerWorkload(userName, firstName, lastName, isActive, existingYears);
			when(workloadRepository.findByTrainerUserName(userName)).thenReturn(workload);
			final var expectedYears =
				List.of(new TrainerWorkload.Year(2030, List.of(new TrainerWorkload.Month(4, 110))));
			final var expected = new TrainerWorkload(userName, firstName, lastName, isActive, expectedYears);

			final boolean result = workloadService.deleteTraining(request);

			assertTrue(result);
			verify(workloadRepository).findByTrainerUserName(userName);
			verify(workloadRepository).saveOfUpdate(expected);
		}
	}
}
