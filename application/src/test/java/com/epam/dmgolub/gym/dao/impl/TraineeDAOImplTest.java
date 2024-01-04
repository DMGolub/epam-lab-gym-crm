package com.epam.dmgolub.gym.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.datasource.IdSequence;
import com.epam.dmgolub.gym.entity.BaseEntity;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.User;
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

class TraineeDAOImplTest {

	@Mock
	private DataSource<Long> dataSource;
	@Mock
	private IdSequence<Long> idSequence;
	@Mock
	private IdSequence<Long> userIdSequence;
	private TraineeDAOImpl traineeDAO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.traineeDAO = new TraineeDAOImpl();
		this.traineeDAO.setDataSource(dataSource);
	}

	@Nested
	class testSave {

		@Test
		void save_shouldAssignIdAndUserIdToTraineeAndSaveEntity_whenInvoked() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainee.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			when(dataSource.getIdSequencesToClasses()).thenReturn(Map.of(
				Trainee.class, idSequence,
				User.class, userIdSequence
			));
			final Long id = 1L;
			when(idSequence.generateNewId()).thenReturn(id);
			final Long userId = 2L;
			when(userIdSequence.generateNewId()).thenReturn(userId);
			final Trainee expected = new Trainee();
			expected.setId(id);
			expected.setUserId(userId);

			try {
				final Trainee result = traineeDAO.save(new Trainee());
				assertEquals(expected, result);
				assertEquals(expected, data.get(Trainee.class).get(id));
				verify(dataSource, times(1)).getData();
				verify(dataSource, times(1)).getIdSequencesToClasses();
				verify(idSequence, times(1)).generateNewId();
				verify(userIdSequence, times(1)).generateNewId();
			} catch (final DAOException e) {
				fail("DAOException is not expected");
			}
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> traineeDAO.save(trainee));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> traineeDAO.save(trainee));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> traineeDAO.save(trainee));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsIllegalArgumentException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new IllegalArgumentException());

			assertThrows(DAOException.class, () -> traineeDAO.save(trainee));
		}
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnOptionalOfTrainee_whenTraineeIsFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final Trainee trainee = new Trainee();
			final Long id = 1L;
			data.put(Trainee.class, Collections.singletonMap(id, trainee));
			when(dataSource.getData()).thenReturn(data);

			final Optional<Trainee> result = traineeDAO.findById(id);

			assertTrue(result.isPresent());
			assertEquals(trainee, result.get());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findById_shouldReturnEmptyOptional_whenTraineeIsNotFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainee.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			final Long id = 99L;

			final Optional<Trainee> result = traineeDAO.findById(id);

			assertFalse(result.isPresent());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> traineeDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> traineeDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestFindAll {

		@Test
		void findAll_shouldReturnTwoTrainees_whenThereAreTwoTrainees() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainee.class, Map.of(1L, new Trainee(), 2L, new Trainee()));
			when(dataSource.getData()).thenReturn(data);
			final List<Trainee> expected = List.of(new Trainee(), new Trainee());

			final var result = traineeDAO.findAll();

			assertEquals(2, result.size());
			assertEquals(expected, result);
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldReturnEmptyList_whenThereAreNoTrainees() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainee.class, Collections.emptyMap());
			when(dataSource.getData()).thenReturn(data);

			assertEquals(0, traineeDAO.findAll().size());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(ClassCastException.class);

			assertThrows(DAOException.class, () -> traineeDAO.findAll());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(NullPointerException.class);

			assertThrows(DAOException.class, () -> traineeDAO.findAll());
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestDelete {

		@Test
		void delete_shouldRemoveTrainee_whenTraineeExists() {
			final Trainee trainee = new Trainee();
			final Long id = 1L;
			trainee.setId(id);
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final Map<Long, BaseEntity<Long>> traineeMap = new HashMap<>();
			traineeMap.put(id, trainee);
			data.put(Trainee.class, traineeMap);
			when(dataSource.getData()).thenReturn(data);

			assertDoesNotThrow(() -> traineeDAO.delete(id));
			assertNull(data.get(Trainee.class).get(id));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void deleteShouldNotThrowExceptions_whenTraineeDoesNotExist() {
			when(dataSource.getData()).thenReturn(Collections.singletonMap(Trainee.class, new HashMap<>()));

			assertDoesNotThrow(() -> traineeDAO.delete(99L));
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> traineeDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> traineeDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void delete_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> traineeDAO.delete(99L));
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldPutGivenEntityToDataSource_whenInvoked() {
			final Trainee trainee = new Trainee();
			final Long id = 1L;
			trainee.setId(id);
			when(dataSource.getData()).thenReturn(Collections.singletonMap(Trainee.class, new HashMap<>()));

			assertDoesNotThrow(() -> traineeDAO.update(trainee));
			verify(dataSource, times(1)).getData();
			assertEquals(trainee, dataSource.getData().get(Trainee.class).get(id));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> traineeDAO.update(trainee));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> traineeDAO.update(trainee));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> traineeDAO.update(trainee));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsIllegalArgumentException() {
			final Trainee trainee = new Trainee();
			when(dataSource.getData()).thenThrow(new IllegalArgumentException());

			assertThrows(DAOException.class, () -> traineeDAO.update(trainee));
		}
	}
}
