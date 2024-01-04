package com.epam.dmgolub.gym.datasource.impl;

import com.epam.dmgolub.gym.datasource.DataSource;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Training;
import com.epam.dmgolub.gym.entity.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {InMemoryDataSource.class})
@TestPropertySource("classpath:application.properties")
class InMemoryDataSourceTest {

	@Autowired
	private DataSource<Long> dataSource;

	private final TrainingType type1;
	private final TrainingType type2;
	private final TrainingType type3;

	private final Trainee trainee1;
	private final Trainee trainee2;
	private final Trainee trainee3;

	private final Trainer trainer1;
	private final Trainer trainer2;
	private final Trainer trainer3;

	private final Training training1;
	private final Training training2;
	private final Training training3;

	{
		type1 = new TrainingType(1L, "Strength training");
		type2 = new TrainingType(2L, "Aerobic exercise");
		type3 = new TrainingType(3L, "Circuit training");

		trainee1 = new Trainee(1L, "Liam", "Anderson", "Liam.Anderson", "GhOYGnECBM", true, 1L, Date.valueOf("1984-9-23"), "4547 Medical Center Drive");
		trainee2 = new Trainee(2L, "Oliver", "Jones", "Oliver.Jones", "FdwRnKyPSM", true, 2L, Date.valueOf("1978-1-5"), "3739 Davis Avenue");
		trainee3 = new Trainee(3L, "James", "Williams", "James.Williams", "cbveybhpMt", true, 3L, Date.valueOf("1988-3-9"), "189 Frosty Lane");

		trainer1 = new Trainer(1L, "Kylian", "Garcia", "Kylian.Garcia", "tuFRGpXIiA", true, 4L, type1);
		trainer2 = new Trainer(2L, "Ezrah", "Jackson", "Ezrah.Jackson", "RRFqJnBunA", true, 5L, type2);
		trainer3 = new Trainer(3L, "Eren", "Miller", "Eren.Miller", "YjvlyVRagP", true, 6L, type3);

		training1 = new Training(1L, trainee1, trainer1, "Test name 1", type1, Date.valueOf("2024-2-1"), 60);
		training2 = new Training(2L, trainee2, trainer2, "Test name 2", type2, Date.valueOf("2024-2-2"), 30);
		training3 = new Training(3L, trainee3, trainer3, "Test name 3", type3, Date.valueOf("2024-2-3"), 45);
	}

	@Test
	void inMemoryDataSource_shouldReadTrainingTypesFromFiles_whenInitialized() {
		final List<TrainingType> expected = List.of(type1, type2, type3);

		final var result = dataSource.getData().get(TrainingType.class).values();

		assertEquals(3, result.size());
		assertTrue(expected.containsAll(result));
	}

	@Test
	void inMemoryDataSource_shouldReadTraineesFromFiles_whenInitialized() {
		final List<Trainee> expected = List.of(trainee1, trainee2, trainee3);

		final var result = dataSource.getData().get(Trainee.class).values();

		assertEquals(3, result.size());
		assertTrue(expected.containsAll(result));
	}

	@Test
	void inMemoryDataSource_shouldReadTrainersFromFiles_whenInitialized() {
		final List<Trainer> expected = List.of(trainer1, trainer2, trainer3);

		final var result = dataSource.getData().get(Trainer.class).values();

		assertEquals(3, result.size());
		assertTrue(expected.containsAll(result));
	}

	@Test
	void inMemoryDataSource_shouldReadTrainingsFromFiles_whenInitialized() {
		final List<Training> expected = List.of(training1, training2, training3);

		final var result = dataSource.getData().get(Training.class).values();

		assertEquals(3, result.size());
		assertTrue(expected.containsAll(result));
	}

	@Test
	void idSequences_shouldReturnNextIdForEachClass_whenInvokedAfterDataSourceInitialization() {
		final var idSequences = dataSource.getIdSequencesToClasses();

		assertEquals(4L, idSequences.get(TrainingType.class).generateNewId());
		assertEquals(4L, idSequences.get(Trainee.class).generateNewId());
		assertEquals(4L, idSequences.get(Trainer.class).generateNewId());
		assertEquals(4L, idSequences.get(Training.class).generateNewId());
	}
}
