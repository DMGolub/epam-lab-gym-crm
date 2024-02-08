package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.UserModel;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private EntityToModelMapper mapper;
	@InjectMocks
	private UserServiceImpl userService;

	@Nested
	class TestFindAll {

		@Test
		void findAll_shouldReturnEmptyList_whenThereAreNoUsers() {
			final List<User> users = Collections.emptyList();
			when(userRepository.findAll()).thenReturn(users);
			final List<UserModel> userModels = Collections.emptyList();
			when(mapper.mapToUserModelList(users)).thenReturn(userModels);

			assertEquals(userModels, userService.findAll());
			verify(userRepository, times(1)).findAll();
			verify(mapper, times(1)).mapToUserModelList(users);
		}

		@Test
		void findAll_shouldReturnTwoUserModels_whenThereAreTwoUsers() {
			final var users = List.of(new User(), new User());
			when(userRepository.findAll()).thenReturn(users);
			final var userModels = List.of(new UserModel(), new UserModel());
			when(mapper.mapToUserModelList(users)).thenReturn(userModels);

			assertEquals(userModels, userService.findAll());
			verify(userRepository, times(1)).findAll();
			verify(mapper, times(1)).mapToUserModelList(users);
		}
	}

	@Nested
	class TestChangeActivityStatus {

		@Test
		void changeActivityStatus_shouldChangeStatusToTrue_whenIsActiveIsFalse() {
			final var user = new User();
			final String userName = "User.Name";
			user.setActive(false);
			user.setUserName(userName);
			when(userRepository.findByUserName(userName)).thenReturn(Optional.of(user));
			final var updatedUser = new User();
			updatedUser.setActive(true);
			updatedUser.setUserName(userName);

			userService.changeActivityStatus(userName);

			verify(userRepository, times(1)).findByUserName(userName);
			verify(userRepository, times(1)).saveAndFlush(updatedUser);
		}

		@Test
		void changeActivityStatus_shouldThrowEntityNotFoundException_whenUserNotFound() {
			final String userName = "User.Name";
			when(userRepository.findByUserName(userName)).thenReturn(Optional.empty());

			assertThrows(EntityNotFoundException.class, () -> userService.changeActivityStatus(userName));
			verify(userRepository, times(1)).findByUserName(userName);
			verifyNoMoreInteractions(userRepository);
		}
	}
}
