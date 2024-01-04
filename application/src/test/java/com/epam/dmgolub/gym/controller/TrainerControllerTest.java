package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TrainerRequestDTO;
import com.epam.dmgolub.gym.dto.TrainerResponseDTO;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINER_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINER_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING_TYPES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private TrainerService trainerService;
	@Mock
	private TrainingTypeService trainingTypeService;
	@InjectMocks
	private TrainerController trainerController;

	@BeforeEach
	void setUp() {
		trainerController = new TrainerController(trainerService, trainingTypeService);
	}

	@Test
	void newTrainer_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final String view = trainerController.newTrainer(model);

		assertEquals(NEW_TRAINER_VIEW_NAME, view);
		verify(trainingTypeService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAINER), any(TrainerRequestDTO.class));
		verify(model).addAttribute(eq(TRAINING_TYPES), any(List.class));
	}

	@Nested
	class TestSave {

		@Test
		void save_shouldReturnNewTrainerPage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);

			final String result = trainerController.save(new TrainerRequestDTO(), bindingResult);

			assertEquals(NEW_TRAINER_VIEW_NAME, result);
			verifyNoInteractions(trainerService);
		}

		@Test
		void save_shouldSaveTrainerAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TrainerRequestDTO trainer = new TrainerRequestDTO();

			final String result = trainerController.save(trainer, bindingResult);

			assertEquals(REDIRECT_TO_TRAINER_INDEX, result);
			verify(trainerService, times(1)).save(trainer);
		}
	}

	@Test
	void edit_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final Long id = 1L;
		final TrainerResponseDTO trainer = new TrainerResponseDTO();
		trainer.setId(id);
		when(trainerService.findById(id)).thenReturn(trainer);

		assertEquals(TRAINER_EDIT_VIEW_NAME, trainerController.edit(id, model));
		verify(trainerService, times(1)).findById(id);
		verify(trainingTypeService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAINER), any(TrainerResponseDTO.class));
		verify(model).addAttribute(eq(TRAINING_TYPES), any(List.class));
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldReturnNewTrainerPage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);
			final TrainerRequestDTO trainer = new TrainerRequestDTO();
			final Long id = 1L;
			trainer.setId(id);

			final String result = trainerController.update(id, trainer, bindingResult);

			assertEquals(TRAINER_EDIT_VIEW_NAME, result);
			verifyNoInteractions(trainerService);
		}

		@Test
		void update_shouldSaveTrainerAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TrainerRequestDTO trainer = new TrainerRequestDTO();
			final Long id = 1L;
			trainer.setId(id);

			final String result = trainerController.update(id, trainer, bindingResult);

			assertEquals(REDIRECT_TO_TRAINER_INDEX, result);
			verify(trainerService, times(1)).update(trainer);
		}
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainerExists() {
		final Long id = 1L;
		final TrainerResponseDTO trainer = new TrainerResponseDTO();
		trainer.setId(id);

		when(trainerService.findById(id)).thenReturn(trainer);

		final String view = trainerController.findById(id, model);

		assertEquals(TRAINER_VIEW_NAME, view);
		verify(trainerService, times(1)).findById(id);
		verify(model).addAttribute(eq(TRAINER), any(TrainerResponseDTO.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainersExist() {
		when(trainerService.findAll()).thenReturn(List.of(new TrainerResponseDTO()));

		final String view = trainerController.findAll(model);

		assertEquals(TRAINER_INDEX_VIEW_NAME, view);
		verify(trainerService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAINERS), any(List.class));
	}
}
