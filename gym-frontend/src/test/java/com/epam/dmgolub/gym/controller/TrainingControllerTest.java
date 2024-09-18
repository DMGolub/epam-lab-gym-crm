package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;

import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINING_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAININGS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_TYPES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private TraineeService traineeService;
	@Mock
	private TrainerService trainerService;
	@Mock
	private TrainingService trainingService;
	@Mock
	private TrainingTypeService trainingTypeService;
	@InjectMocks
	private TrainingController trainingController;
	@Mock
	private HttpSession session;

	@Nested
	class TestSave {

		@Test
		void save_shouldReturnNewTrainingPage_whenBingingResultHasErrors() {
			final var request = new TrainingCreateRequestDTO();
			final String userName = "User.Name1";
			request.setTraineeUserName(userName);
			when(bindingResult.hasErrors()).thenReturn(true);
			final var trainee = (new TraineeResponseDTO());
			when(traineeService.findByUserName(userName, session)).thenReturn(trainee);
			when(trainerService.findActiveTrainersAssignedOnTrainee(userName, session))
				.thenReturn(Collections.emptyList());

			final String result = trainingController.save(request, bindingResult, model, session);

			assertEquals(NEW_TRAINING_VIEW_NAME, result);
			verifyNoInteractions(trainingService);
		}

		@Test
		void save_shouldSaveTrainingAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final var request = new TrainingCreateRequestDTO();
			final var training = new TrainingCreateRequestDTO();

			final String result = trainingController.save(request, bindingResult, model, session);

			assertEquals(REDIRECT_TO_TRAINING_INDEX, result);
			verify(trainingService, times(1)).save(training, session);
		}
	}

	@Nested
	class testSearchByTrainee {

		@Test
		void searchByTrainee_shouldReturnTrainingIndexPage_whenRequestIsValid() {
			final var request =
				new TraineeTrainingsSearchRequestDTO("TraineeName", null, null, null, null);
			when(trainingTypeService.findAll()).thenReturn(Collections.emptyList());
			when(bindingResult.hasErrors()).thenReturn(false);

			final var result = trainingController.searchByTrainee(request, bindingResult, model, session);

			assertEquals(TRAINING_INDEX_VIEW_NAME, result);
			verify(bindingResult, times(1)).hasErrors();
			model.addAttribute(eq(TRAINING_TYPES), any(List.class));
			model.addAttribute(eq(TRAININGS), any(List.class));
		}

		@Test
		void searchByTrainee_shouldReturnTrainingIndexPage_whenBingingResultHasErrors() {
			final var request =
				new TraineeTrainingsSearchRequestDTO("TraineeName", null, null, null, null);
			when(trainingTypeService.findAll()).thenReturn(Collections.emptyList());
			when(bindingResult.hasErrors()).thenReturn(true);

			final var result = trainingController.searchByTrainee(request, bindingResult, model, session);

			assertEquals(TRAINING_INDEX_VIEW_NAME, result);
			model.addAttribute(eq(TRAINING_TYPES), any(List.class));
			verify(bindingResult, times(1)).hasErrors();
		}
	}

	@Nested
	class testSearchByTrainer {

		@Test
		void searchByTrainer_shouldReturnTrainingIndexPage_whenRequestIsValid() {
			final var request =
				new TrainerTrainingsSearchRequestDTO("TrainerName", null, null, null);
			when(trainingTypeService.findAll()).thenReturn(Collections.emptyList());
			when(bindingResult.hasErrors()).thenReturn(false);

			final var result = trainingController.searchByTrainer(request, bindingResult, model, session);

			assertEquals(TRAINING_INDEX_VIEW_NAME, result);
			verify(bindingResult, times(1)).hasErrors();
			model.addAttribute(eq(TRAINING_TYPES), any(List.class));
			model.addAttribute(eq(TRAININGS), any(List.class));
		}

		@Test
		void searchByTrainer_shouldReturnTrainingIndexPage_whenBingingResultHasErrors() {
			final var request =
				new TrainerTrainingsSearchRequestDTO("TrainerName", null, null, null);
			when(trainingTypeService.findAll()).thenReturn(Collections.emptyList());
			when(bindingResult.hasErrors()).thenReturn(true);

			final var result = trainingController.searchByTrainer(request, bindingResult, model, session);

			assertEquals(TRAINING_INDEX_VIEW_NAME, result);
			model.addAttribute(eq(TRAINING_TYPES), any(List.class));
			verify(bindingResult, times(1)).hasErrors();
		}
	}
}
