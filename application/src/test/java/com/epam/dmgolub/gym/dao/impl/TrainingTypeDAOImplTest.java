package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.BaseEntity;
import com.epam.dmgolub.gym.entity.TrainingType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingTypeDAOImplTest {

	@Mock
	private DataSource<Long> dataSource;
	private TrainingTypeDAOImpl trainingTypeDAO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		trainingTypeDAO = new TrainingTypeDAOImpl();
		trainingTypeDAO.setDataSource(dataSource);
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnOptionalOfTrainingType_whenTrainingTypeIsFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final TrainingType trainingType = new TrainingType();
			final Long id = 1L;
			data.put(TrainingType.class, Collections.singletonMap(id, trainingType));
			when(dataSource.getData()).thenReturn(data);

			final Optional<TrainingType> result = trainingTypeDAO.findById(id);

			assertTrue(result.isPresent());
			assertEquals(trainingType, result.get());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findById_shouldReturnEmptyOptional_whenTrainingTypeIsNotFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(TrainingType.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			final Long id = 99L;

			final Optional<TrainingType> result = trainingTypeDAO.findById(id);

			assertFalse(result.isPresent());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainingTypeDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainingTypeDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestFindAll {

		@Test
		void findAll_shouldReturnTwoTrainingTypes_whenThereAreTwoTrainingTypes() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(TrainingType.class, Map.of(1L, new TrainingType(), 2L, new TrainingType()));
			when(dataSource.getData()).thenReturn(data);
			final List<TrainingType> expected = List.of(new TrainingType(), new TrainingType());

			final var result = trainingTypeDAO.findAll();

			assertEquals(2, result.size());
			assertEquals(expected, result);
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldReturnEmptyList_whenThereAreNoTrainingTypes() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(TrainingType.class, Collections.emptyMap());
			when(dataSource.getData()).thenReturn(data);

			assertEquals(0, trainingTypeDAO.findAll().size());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(ClassCastException.class);

			assertThrows(DAOException.class, () -> trainingTypeDAO.findAll());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(NullPointerException.class);

			assertThrows(DAOException.class, () -> trainingTypeDAO.findAll());
			verify(dataSource, times(1)).getData();
		}
	}
}
