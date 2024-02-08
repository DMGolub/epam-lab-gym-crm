package com.epam.dmgolub.gym.datasource;

import com.epam.dmgolub.gym.repository.TraineeRepository;
import com.epam.dmgolub.gym.repository.TrainerRepository;
import com.epam.dmgolub.gym.repository.TrainingRepository;
import com.epam.dmgolub.gym.repository.TrainingTypeRepository;
import com.epam.dmgolub.gym.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
	"dummy.data.initialization=true",
	"spring.datasource.url=jdbc:tc:postgresql:9.6.8:///gymtest",
	"spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class DummyDataInitializerTest {

	@Autowired
	private TraineeRepository traineeRepository;
	@Autowired
	private TrainerRepository trainerRepository;
	@Autowired
	private TrainingRepository trainingRepository;
	@Autowired
	private TrainingTypeRepository trainingTypeRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldPopulateDatabaseWithDummyData() {
		final var trainees = traineeRepository.findAll();
		final var trainers = trainerRepository.findAll();
		final var trainings = trainingRepository.findAll();
		final var trainingTypes = trainingTypeRepository.findAll();
		final var users = userRepository.findAll();

		assertEquals(3, trainees.size());
		assertEquals(3, trainers.size());
		assertEquals(3, trainings.size());
		assertEquals(3, trainingTypes.size());
		assertEquals(6, users.size());
	}
}
