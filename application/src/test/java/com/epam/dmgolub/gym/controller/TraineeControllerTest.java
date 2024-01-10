package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingRequestDTO;
import com.epam.dmgolub.gym.dto.TrainingResponseDTO;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

import static com.epam.dmgolub.gym.controller.constant.Constants.AVAILABLE_TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_NEW_TRAINEE;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINEE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEES;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

	@Mock
	private Model model;
	@Mock
	private BindingResult bindingResult;
	@Mock
	private RedirectAttributes redirectAttributes;
	@Mock
	private TraineeService traineeService;
	@Mock
	private TrainerService trainerService;
	@InjectMocks
	private TraineeController traineeController;

	@BeforeEach
	void setUp() {
		traineeController = new TraineeController(traineeService, trainerService);
	}

	@Test
	void newTrainee_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final String view = traineeController.newTrainee(model);

		assertEquals(NEW_TRAINEE_VIEW_NAME, view);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeRequestDTO.class));
	}

	@Test
	void addTraining_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final Long id = 1L;
		when(traineeService.findById(id)).thenReturn(new TraineeResponseDTO());
		when(trainerService.findActiveTrainersAssignedToTrainee(id)).thenReturn(Collections.emptyList());

		assertEquals(NEW_TRAINING_VIEW_NAME, traineeController.addTraining(id, model));
		verify(traineeService, times(1)).findById(id);
		verify(trainerService, times(1)).findActiveTrainersAssignedToTrainee(id);
		verify(model).addAttribute(eq(TRAINING), any(TrainingRequestDTO.class));
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(TRAINERS), any(List.class));
	}

	@Nested
	class TestSave {

		@Test
		void save_shouldReturnNewTraineePage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);

			final String result = traineeController.save(new TraineeRequestDTO(), bindingResult, redirectAttributes);

			assertEquals(NEW_TRAINEE_VIEW_NAME, result);
			verifyNoInteractions(traineeService);
		}

		@Test
		void save_shouldSaveTraineeAndRedirectToNewTraineePage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TraineeRequestDTO trainee = new TraineeRequestDTO();

			final String result = traineeController.save(trainee, bindingResult, redirectAttributes);

			assertEquals(REDIRECT_TO_NEW_TRAINEE, result);
			verify(traineeService, times(1)).save(trainee);
		}
	}

	@Nested
	class TestHandleActionByUserName {

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsFindAndTraineeExists() {
			final String userName = "UserName";
			final var trainee = new TraineeResponseDTO();
			final Long traineeId = 2L;
			trainee.setId(traineeId);
			when(traineeService.findByUserName(userName)).thenReturn(trainee);

			final String result =
				traineeController.handleActionByUserName("find", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX + traineeId, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsDeleteAndTraineeExists() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName)).thenReturn(new TraineeResponseDTO());

			final String result =
				traineeController.handleActionByUserName("delete", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(traineeService, times(1)).delete(userName);
			verify(redirectAttributes).addFlashAttribute(eq(SUCCESS_MESSAGE_ATTRIBUTE), any(String.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineeIndex_whenTraineeNotFound() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName)).thenThrow(new EntityNotFoundException("Not found"));

			final String result =
				traineeController.handleActionByUserName("find", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineeIndex_whenActionUnknown() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName)).thenReturn(new TraineeResponseDTO());

			final String result =
				traineeController.handleActionByUserName("unknown", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}
	}

	@Test
	void edit_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final Long id = 1L;
		final TraineeResponseDTO trainee = new TraineeResponseDTO();
		trainee.setId(id);
		when(traineeService.findById(id)).thenReturn(trainee);

		assertEquals(TRAINEE_EDIT_VIEW_NAME, traineeController.edit(id, model));
		verify(traineeService, times(1)).findById(id);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
	}

	@Nested
	class TestUpdate {
		@Test
		void update_shouldReturnNewTraineePage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);
			final TraineeRequestDTO trainee = new TraineeRequestDTO();
			final Long id = 1L;
			trainee.setId(id);

			final String result = traineeController.update(id, trainee, bindingResult);

			assertEquals(TRAINEE_EDIT_VIEW_NAME, result);
			verifyNoInteractions(traineeService);
		}

		@Test
		void update_shouldSaveTraineeAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TraineeRequestDTO trainee = new TraineeRequestDTO();
			final Long id = 1L;
			trainee.setId(id);

			final String result = traineeController.update(id, trainee, bindingResult);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).update(trainee);
		}
	}

	@Test
	void addTrainer_shouldAddTrainerAndRedirectToTraineePage_whenInvokedWithParams() {
		final Long traineeId = 1L;
		final Long trainerId = 2L;
		when(traineeService.findById(traineeId)).thenReturn(new TraineeResponseDTO());
		when(trainerService.findActiveTrainersNotAssignedToTrainee(traineeId)).thenReturn(Collections.emptyList());

		final String result = traineeController.addTrainer(traineeId, trainerId, model);

		assertEquals(REDIRECT_TO_TRAINEE_INDEX + traineeId, result);
		verify(traineeService, times(1)).addTrainer(traineeId, trainerId);
		verify(traineeService, times(1)).findById(traineeId);
		verify(trainerService, times(1)).findActiveTrainersNotAssignedToTrainee(traineeId);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(AVAILABLE_TRAINERS), any(List.class));
	}

	@Test
	void delete_shouldInvokeServiceAndRedirectToTraineeIndexPage_whenInvoked() {
		final Long id = 1L;

		final String view = traineeController.delete(id);

		assertEquals(REDIRECT_TO_TRAINEE_INDEX, view);
		verify(traineeService, times(1)).delete(id);
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainerExists() {
		final Long id = 1L;
		final TraineeResponseDTO trainee = new TraineeResponseDTO();
		trainee.setId(id);

		when(traineeService.findById(id)).thenReturn(trainee);

		final String view = traineeController.findById(id, model);

		assertEquals(TRAINEE_VIEW_NAME, view);
		verify(traineeService, times(1)).findById(id);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTraineesExist() {
		when(traineeService.findAll()).thenReturn(List.of(new TraineeResponseDTO()));

		final String view = traineeController.findAll(model);

		assertEquals(TRAINEE_INDEX_VIEW_NAME, view);
		verify(traineeService, times(1)).findAll();
		verify(model).addAttribute(eq(TRAINEES), any(List.class));
	}
}
