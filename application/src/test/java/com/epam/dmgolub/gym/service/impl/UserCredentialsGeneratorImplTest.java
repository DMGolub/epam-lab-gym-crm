package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dao.TraineeDAO;
import com.epam.dmgolub.gym.dao.TrainerDAO;
import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCredentialsGeneratorImplTest {

	@Value("${password.generated.length}")
	private static int passwordLength;
	@Mock
	private TraineeDAO traineeDAO;
	@Mock
	private TrainerDAO trainerDAO;
	@InjectMocks
	private UserCredentialsGeneratorImpl generator;

	@Test
	void generateUserName_shouldNotAddSuffix_whenThereAreNoOtherUsers() {
		final Trainee trainee = new Trainee();
		trainee.setFirstName("First");
		trainee.setLastName("Last");
		when(trainerDAO.findAll()).thenReturn(Collections.emptyList());
		when(traineeDAO.findAll()).thenReturn(Collections.emptyList());

		assertEquals("First.Last", generator.generateUserName(trainee));
	}

	@Test
	void generateUserName_shouldAddSuffixTwo_whenThereIsOneUserWithEqualUserName() {
		final Trainee trainee = new Trainee();
		trainee.setFirstName("First");
		trainee.setLastName("Last");
		trainee.setUserName("First.Last");
		final Trainer trainer = new Trainer();
		trainer.setFirstName("First");
		trainer.setLastName("Last");
		when(trainerDAO.findAll()).thenReturn(Collections.emptyList());
		when(traineeDAO.findAll()).thenReturn(List.of(trainee));

		assertEquals("First.Last2", generator.generateUserName(trainee));
		assertEquals("First.Last2", generator.generateUserName(trainer));
	}

	@Test
	void generateUserName_shouldAddNextSuffix_whenThereAreSeveralUsersWithEqualUserName() {
		final Trainee trainee = new Trainee();
		trainee.setFirstName("First");
		trainee.setLastName("Last");
		trainee.setUserName("First.Last");
		final Trainer trainer = new Trainer();
		trainer.setFirstName("First");
		trainer.setLastName("Last");
		trainer.setUserName("First.Last3");
		when(trainerDAO.findAll()).thenReturn(List.of(trainer));
		when(traineeDAO.findAll()).thenReturn(List.of(trainee));

		assertEquals("First.Last4", generator.generateUserName(trainee));
		assertEquals("First.Last4", generator.generateUserName(trainer));
	}

	@Test
	void generatePassword_shouldGeneratePasswordWithRequiredLength_whenInvoked() {
		final Trainee trainee = new Trainee();
		final Trainer trainer = new Trainer();

		final String traineePassword = generator.generatePassword(trainee);
		final String trainerPassword = generator.generatePassword(trainer);

		assertEquals(passwordLength, traineePassword.length());
		assertEquals(passwordLength, trainerPassword.length());
	}
}
