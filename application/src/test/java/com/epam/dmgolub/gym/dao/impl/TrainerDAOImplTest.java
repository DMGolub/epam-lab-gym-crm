package com.epam.dmgolub.gym.dao.impl;

import com.epam.dmgolub.gym.dao.exception.DAOException;
import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.datasource.IdSequence;
import com.epam.dmgolub.gym.entity.BaseEntity;
import com.epam.dmgolub.gym.entity.Trainer;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerDAOImplTest {

	@Mock
	private DataSource<Long> dataSource;
	@Mock
	private IdSequence<Long> idSequence;
	@Mock
	private IdSequence<Long> userIdSequence;
	private TrainerDAOImpl trainerDAO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		trainerDAO = new TrainerDAOImpl();
		trainerDAO.setDataSource(dataSource);
	}

	@Nested
	class TestSave {

		@Test
		void save_shouldAssignIdAndUserIdToTrainerAndSaveEntity_whenInvoked() {
			final Trainer trainer = new Trainer();
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainer.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			when(dataSource.getIdSequencesToClasses()).thenReturn(Map.of(
				Trainer.class, idSequence,
				User.class, userIdSequence
			));
			final Long id = 1L;
			when(idSequence.generateNewId()).thenReturn(id);
			final Long userId = 2L;
			when(userIdSequence.generateNewId()).thenReturn(userId);
			final Trainer expected = new Trainer();
			expected.setId(id);
			expected.setUserId(userId);

			try {
				final Trainer result = trainerDAO.save(trainer);
				assertEquals(expected, result);
				assertEquals(expected, data.get(Trainer.class).get(id));
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
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainerDAO.save(trainer));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainerDAO.save(trainer));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> trainerDAO.save(trainer));
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsIllegalArgumentException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new IllegalArgumentException());

			assertThrows(DAOException.class, () -> trainerDAO.save(trainer));
		}
	}

	@Nested
	class TestFindById {

		@Test
		void findById_shouldReturnOptionalOfTrainer_whenTrainerIsFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			final Trainer trainer = new Trainer();
			final Long id = 1L;
			data.put(Trainer.class, Collections.singletonMap(id, trainer));
			when(dataSource.getData()).thenReturn(data);

			final Optional<Trainer> result = trainerDAO.findById(id);

			assertTrue(result.isPresent());
			assertEquals(trainer, result.get());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findById_shouldReturnEmptyOptional_whenTrainerIsNotFound() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainer.class, new HashMap<>());
			when(dataSource.getData()).thenReturn(data);
			final Long id = 99L;

			final Optional<Trainer> result = trainerDAO.findById(id);

			assertFalse(result.isPresent());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainerDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}

		@Test
		void save_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainerDAO.findById(99L));
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestFindAll {

		@Test
		void findAll_shouldReturnTwoTrainers_whenThereAreTwoTrainers() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainer.class, Map.of(1L, new Trainer(), 2L, new Trainer()));
			when(dataSource.getData()).thenReturn(data);
			final List<Trainer> expected = List.of(new Trainer(), new Trainer());

			final var result = trainerDAO.findAll();

			assertEquals(2, result.size());
			assertEquals(expected, result);
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldReturnEmptyList_whenThereAreNoTrainers() {
			final Map<Class<?>, Map<Long, BaseEntity<Long>>> data = new HashMap<>();
			data.put(Trainer.class, Collections.emptyMap());
			when(dataSource.getData()).thenReturn(data);

			assertEquals(0, trainerDAO.findAll().size());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsClassCastException() {
			when(dataSource.getData()).thenThrow(ClassCastException.class);

			assertThrows(DAOException.class, () -> trainerDAO.findAll());
			verify(dataSource, times(1)).getData();
		}

		@Test
		void findAll_shouldThrowDAOException_when_dataSourceThrowsNullPointerException() {
			when(dataSource.getData()).thenThrow(NullPointerException.class);

			assertThrows(DAOException.class, () -> trainerDAO.findAll());
			verify(dataSource, times(1)).getData();
		}
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldPutGivenEntityToDataSource_whenInvoked() {
			final Trainer trainer = new Trainer();
			final Long id = 1L;
			trainer.setId(id);
			when(dataSource.getData()).thenReturn(Collections.singletonMap(Trainer.class, new HashMap<>()));

			assertDoesNotThrow(() -> trainerDAO.update(trainer));
			verify(dataSource, times(1)).getData();
			assertEquals(trainer, dataSource.getData().get(Trainer.class).get(id));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsClassCastException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new ClassCastException());

			assertThrows(DAOException.class, () -> trainerDAO.update(trainer));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsNullPointerException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new NullPointerException());

			assertThrows(DAOException.class, () -> trainerDAO.update(trainer));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsUnsupportedOperationException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new UnsupportedOperationException());

			assertThrows(DAOException.class, () -> trainerDAO.update(trainer));
		}

		@Test
		void update_shouldThrowDAOException_whenDataSourceThrowsIllegalArgumentException() {
			final Trainer trainer = new Trainer();
			when(dataSource.getData()).thenThrow(new IllegalArgumentException());

			assertThrows(DAOException.class, () -> trainerDAO.update(trainer));
		}
	}
}
