package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.epam.dmgolub.gym.controller.constant.Constants.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private TrainingService trainingService;
	@Mock
	private TraineeService traineeService;
	@Mock
	private TrainerService trainerService;
	@Mock
	private TrainingTypeService trainingTypeService;
	@InjectMocks
	private TrainingController trainingController;

	@Test
	void newTraining_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		assertEquals(NEW_TRAINING_VIEW_NAME, trainingController.newTraining(model));
		verify(traineeService, times(1)).findAll();
		verify(trainerService, times(1)).findAll();
		verify(trainingTypeService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAINING), any(TrainingRequestDTO.class));
		verify(model).addAttribute(eq(TRAINEES), any(List.class));
		verify(model).addAttribute(eq(TRAINERS), any(List.class));
		verify(model).addAttribute(eq(TRAINING_TYPES), any(List.class));
	}

	@Nested
	class TestSave {
		@Test
		void save_shouldReturnNewTrainingPage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);

			final String result = trainingController.save(new TrainingRequestDTO(), bindingResult);

			assertEquals(NEW_TRAINING_VIEW_NAME, result);
			verifyNoInteractions(trainingService);
		}

		@Test
		void save_shouldSaveTrainingAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TrainingRequestDTO training = new TrainingRequestDTO();

			final String result = trainingController.save(training, bindingResult);

			assertEquals(REDIRECT_TO_TRAINING_INDEX, result);
			verify(trainingService, times(1)).save(training);
		}
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainingExists() {
		long id = 1L;
		when(trainingService.findById(id)).thenReturn(new TrainingResponseDTO());

		final String result = trainingController.findById(id, model);

		assertEquals(TRAINING_VIEW_NAME, result);
		verify(trainingService).findById(id);
		verify(model).addAttribute(eq(TRAINING), any(TrainingResponseDTO.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainingsExist() {
		when(trainingService.findAll()).thenReturn(List.of(new TrainingResponseDTO()));

		final String result = trainingController.findAll(model);

		assertEquals(TRAINING_INDEX_VIEW_NAME, result);
		verify(trainingService).findAll();
		verify(model).addAttribute(eq(TRAININGS), any(List.class));
	}
}
