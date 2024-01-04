package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.datasource.IdSequence;
import com.epam.dmgolub.gym.entity.BaseEntity;
import com.epam.dmgolub.gym.entity.Training;

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

class TrainingDAOImplTest {

	@Mock
	private DataSource<Long> dataSource;
	@Mock
	private IdSequence<Long> idSequence;
	private TrainingDAOImpl trainingDAO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		trainingDAO = new TrainingDAOImpl();
		trainingDAO.setDataSource(dataSource);
	}

	@Nested
	class TestSave {

		@Test
		void save_shouldAssignIdToTrainingAndSaveEntity_whenInvoked() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Training.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			when(dataSource.getIdSequencesToClasses()).thenReturn(Map.of(Training.class, idSequence));
			final Long id = 1L;
			when(idSequence.generateNewId()).thenReturn(id);
			final Training expected = new Training();
			expected.setId(id);

			try {
				final Training result = trainingDAO.save(new Training());
				assertEquals(expected, result);
				assertEquals(expected, data.get(Training.class).get(id));
				verify(dataSource, times(1)).getData();
				verify(dataSource, times(1)).getIdSequencesToClasses();
				verify(idSequence, times(1)).generateNewId();
			} catch (final DAOException e) {
				fail("DAOException is not expected");
			}
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			final Training training = new Training();
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainingDAO.save(training));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			final Training training = new Training();
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainingDAO.save(training));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			final Training training = new Training();
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> trainingDAO.save(training));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsIllegalArgumentException() {
			final Training training = new Training();
			when(dataSource.getData()).thenThrow(new IllegalArgumentException());

			assertThrows(DAOException.class, () -> trainingDAO.save(training));
		}
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnOptionalOfTraining_whenTrainingIsFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final Training training = new Training();
			final Long id = 1L;
			data.put(Training.class, Collections.singletonMap(id, training));
			when(dataSource.getData()).thenReturn(data);

			final Optional<Training> result = trainingDAO.findById(id);

			assertTrue(result.isPresent());
			assertEquals(training, result.get());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findById_shouldReturnEmptyOptional_whenTrainingIsNotFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Training.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			final Long id = 99L;

			final Optional<Training> result = trainingDAO.findById(id);

			assertFalse(result.isPresent());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainingDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainingDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestFindAll {

		@Test
		void findAll_shouldReturnTwoTrainings_whenThereAreTwoTrainings() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Training.class, Map.of(1L, new Training(), 2L, new Training()));
			when(dataSource.getData()).thenReturn(data);
			final List<Training> expected = List.of(new Training(), new Training());

			final var result = trainingDAO.findAll();

			assertEquals(2, result.size());
			assertEquals(expected, result);
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldReturnEmptyList_whenThereAreNoTrainings() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Training.class, Collections.emptyMap());
			when(dataSource.getData()).thenReturn(data);

			assertEquals(0, trainingDAO.findAll().size());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(ClassCastException.class);

			assertThrows(DAOException.class, () -> trainingDAO.findAll());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(NullPointerException.class);

			assertThrows(DAOException.class, () -> trainingDAO.findAll());
			verify(dataSource, times(1)).getData();
		}
	}
	
	@Nested
	class TestDelete {

		@Test
		void delete_shouldRemoveTraining_whenTrainingExists() {
			final Training training = new Training();
			final Long id = 1L;
			training.setId(id);
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final Map<Long, BaseEntity<Long>> trainingMap = new HashMap<>();
			trainingMap.put(id, training);
			data.put(Training.class, trainingMap);
			when(dataSource.getData()).thenReturn(data);

			assertDoesNotThrow(() -> trainingDAO.delete(id));
			assertNull(data.get(Training.class).get(id));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void deleteShouldNotThrowExceptions_whenTrainingDoesNotExist() {
			when(dataSource.getData()).thenReturn(Collections.singletonMap(Training.class, new HashMap<>()));

			assertDoesNotThrow(() -> trainingDAO.delete(99L));
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainingDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainingDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> trainingDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}
	}
}
