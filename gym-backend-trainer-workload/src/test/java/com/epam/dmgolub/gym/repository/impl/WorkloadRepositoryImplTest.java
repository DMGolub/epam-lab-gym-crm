package com.epam.dmgolub.gym.repository.impl;

import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.TrainerWorkload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkloadRepositoryImplTest {

	@Mock
	private DataSource dataSource;
	@InjectMocks
	private WorkloadRepositoryImpl workloadRepository;

	@Test
	void findByTrainerUserName_shouldReturnTrainerWorkload_whenTrainerWorkloadExists() {
		final var userName = "FirstName.LastName";
		final var expected =
			new TrainerWorkload(userName, "FirstName", "LastName", true, new ArrayList<>());
		final var data = Map.of(userName, expected);
		when(dataSource.getData()).thenReturn(data);

		final var result = workloadRepository.findByTrainerUserName(userName);

		assertEquals(expected, result);
		verify(dataSource).getData();
	}

	@Test
	void saveOfUpdate_shouldSaveTrainerWorkload_whenWorkloadDoesNotExist() {
		final var data = new HashMap<String, TrainerWorkload>();
		final var trainerWorkload = new TrainerWorkload(
			"FirstName.LastName",
			"FirstName",
			"LastName",
			true,
			List.of(new TrainerWorkload.Year(2025, List.of(new TrainerWorkload.Month(4, 90))))
		);
		when(dataSource.getData()).thenReturn(data);

		workloadRepository.saveOfUpdate(trainerWorkload);
		assertEquals(1, data.size());
	}
}
