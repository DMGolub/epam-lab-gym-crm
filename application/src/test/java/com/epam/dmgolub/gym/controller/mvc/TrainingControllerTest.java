package com.epam.dmgolub.gym.controller.mvc;

import com.epam.dmgolub.gym.dto.mvc.TraineeTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainerTrainingsSearchRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingResponseDTO;
import com.epam.dmgolub.gym.mapper.mvc.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.*;

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
	@Mock
	private ModelToDtoMapper mapper;
	@InjectMocks
	private TrainingController trainingController;

	@Nested
	class TestSave {

		@Test
		void save_shouldReturnNewTrainingPage_whenBingingResultHasErrors() {
			final var request = new TrainingRequestDTO();
			final String userName = "User.Name1";
			request.setTraineeUserName(userName);
			when(bindingResult.hasErrors()).thenReturn(true);
			final var trainee = (new TraineeModel());
			when(traineeService.findByUserName(userName)).thenReturn(trainee);
			when(trainerService.findActiveTrainersAssignedOnTrainee(userName)).thenReturn(Collections.emptyList());

			final String result = trainingController.save(request, bindingResult, model);

			assertEquals(NEW_TRAINING_VIEW_NAME, result);
			verifyNoInteractions(trainingService);
		}

		@Test
		void save_shouldSaveTrainingAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final var request = new TrainingRequestDTO();
			final var training = new TrainingModel();
			when(mapper.mapToTrainingModel(request)).thenReturn(training);

			final String result = trainingController.save(request, bindingResult, model);

			assertEquals(REDIRECT_TO_TRAINING_INDEX, result);
			verify(mapper, times(1)).mapToTrainingModel(request);
			verify(trainingService, times(1)).save(training);
		}
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainingExists() {
		long id = 1L;
		final var training = new TrainingModel();
		when(trainingService.findById(id)).thenReturn(training);
		final var response = new TrainingResponseDTO();
		when(mapper.mapTrainingResponseDTO(training)).thenReturn(response);

		final String result = trainingController.findById(id, model);

		assertEquals(TRAINING_VIEW_NAME, result);
		verify(trainingService, times(1)).findById(id);
		verify(mapper, times(1)).mapTrainingResponseDTO(training);
		verify(model).addAttribute(eq(TRAINING), any(TrainingResponseDTO.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainingsExist() {
		when(trainingService.findAll()).thenReturn(List.of(new TrainingModel()));
		when(trainingTypeService.findAll()).thenReturn(List.of(new TrainingTypeModel()));

		final String result = trainingController.findAll(model);

		assertEquals(TRAINING_INDEX_VIEW_NAME, result);
		verify(trainingService, times(1)).findAll();
		verify(trainingTypeService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAININGS), any(List.class));
		verify(model).addAttribute(eq(TRAINING_TYPES), any(List.class));
	}

	@Nested
	class testSearchByTrainee {

		@Test
		void searchByTrainee_shouldReturnTrainingIndexPage_whenRequestIsValid() {
			final var request =
				new TraineeTrainingsSearchRequestDTO("TraineeName", null, null, null, null);
			when(trainingTypeService.findAll()).thenReturn(Collections.emptyList());
			when(bindingResult.hasErrors()).thenReturn(false);

			final var result = trainingController.searchByTrainee(request, bindingResult, model);

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

			final var result = trainingController.searchByTrainee(request, bindingResult, model);

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

			final var result = trainingController.searchByTrainer(request, bindingResult, model);

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

			final var result = trainingController.searchByTrainer(request, bindingResult, model);

			assertEquals(TRAINING_INDEX_VIEW_NAME, result);
			model.addAttribute(eq(TRAINING_TYPES), any(List.class));
			verify(bindingResult, times(1)).hasErrors();
		}
	}
}
