package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.service.TraineeService;
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

import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINEE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEES;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.TRAINEE_VIEW_NAME;
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
	private TraineeService traineeService;
	@InjectMocks
	private TraineeController traineeController;

	@BeforeEach
	void setUp() {
		traineeController = new TraineeController(traineeService);
	}

	@Test
	void newTrainee_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final String view = traineeController.newTrainee(model);

		assertEquals(NEW_TRAINEE_VIEW_NAME, view);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeRequestDTO.class));
	}

	@Nested
	class TestSave {

		@Test
		void save_shouldReturnNewTraineePage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);

			final String result = traineeController.save(new TraineeRequestDTO(), bindingResult);

			assertEquals(NEW_TRAINEE_VIEW_NAME, result);
			verifyNoInteractions(traineeService);
		}

		@Test
		void save_shouldSaveTraineeAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final TraineeRequestDTO trainee = new TraineeRequestDTO();

			final String result = traineeController.save(trainee, bindingResult);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).save(trainee);
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
