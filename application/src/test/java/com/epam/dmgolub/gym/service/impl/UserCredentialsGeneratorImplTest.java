package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.Trainer;
import com.epam.dmgolub.gym.entity.Trainee;
import com.epam.dmgolub.gym.repository.UserRepository;
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
	private UserRepository userRepository;
	@InjectMocks
	private UserCredentialsGeneratorImpl generator;

	@Test
	void generateUserName_shouldNotAddSuffix_whenThereAreNoOtherUsers() {
		final Trainee trainee = new Trainee();
		trainee.getUser().setFirstName("First");
		trainee.getUser().setLastName("Last");
		when(userRepository.findAll()).thenReturn(Collections.emptyList());

		assertEquals("First.Last", generator.generateUserName(trainee.getUser()));
	}

	@Test
	void generateUserName_shouldAddSuffixTwo_whenThereIsOneUserWithEqualUserName() {
		final Trainee trainee = new Trainee();
		trainee.getUser().setFirstName("First");
		trainee.getUser().setLastName("Last");
		trainee.getUser().setUserName("First.Last");
		final Trainer trainer = new Trainer();
		trainer.getUser().setFirstName("First");
		trainer.getUser().setLastName("Last");
		when(userRepository.findAll()).thenReturn(List.of(trainee.getUser()));

		assertEquals("First.Last2", generator.generateUserName(trainee.getUser()));
		assertEquals("First.Last2", generator.generateUserName(trainer.getUser()));
	}

	@Test
	void generateUserName_shouldAddNextSuffix_whenThereAreSeveralUsersWithEqualUserName() {
		final Trainee trainee = new Trainee();
		trainee.getUser().setFirstName("First");
		trainee.getUser().setLastName("Last");
		trainee.getUser().setUserName("First.Last");
		final Trainer trainer = new Trainer();
		trainer.getUser().setFirstName("First");
		trainer.getUser().setLastName("Last");
		trainer.getUser().setUserName("First.Last3");
		when(userRepository.findAll()).thenReturn(List.of(trainer.getUser(), trainee.getUser()));

		assertEquals("First.Last4", generator.generateUserName(trainee.getUser()));
		assertEquals("First.Last4", generator.generateUserName(trainer.getUser()));
	}

	@Test
	void generatePassword_shouldGeneratePasswordWithRequiredLength_whenInvoked() {
		final Trainee trainee = new Trainee();
		final Trainer trainer = new Trainer();

		final String traineePassword = generator.generatePassword(trainee.getUser());
		final String trainerPassword = generator.generatePassword(trainer.getUser());

		assertEquals(passwordLength, traineePassword.length());
		assertEquals(passwordLength, trainerPassword.length());
	}
}
