package com.epam.dmgolub.gym.controller;

import com.epam.dmgolub.gym.dto.CredentialsDTO;
import com.epam.dmgolub.gym.dto.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.TrainingCreateRequestDTO;
import com.epam.dmgolub.gym.service.TraineeService;
import com.epam.dmgolub.gym.service.TrainerService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
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

import java.util.ArrayList;
import java.util.List;

import static com.epam.dmgolub.gym.controller.constant.Constants.AVAILABLE_TRAINERS;
import static com.epam.dmgolub.gym.controller.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_NEW_TRAINEE;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINEE_INDEX;
import static com.epam.dmgolub.gym.controller.constant.Constants.REDIRECT_TO_TRAINEE_PROFILE;
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
	@Mock
	private HttpSession session;

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
		final String userName = "User.Name";
		final var trainee = new TraineeResponseDTO();
		when(traineeService.findByUserName(userName, session)).thenReturn(trainee);
		final var trainers = new ArrayList<TraineeResponseDTO.TrainerDTO>();
		when(trainerService.findActiveTrainersAssignedOnTrainee(userName, session)).thenReturn(trainers);

		assertEquals(NEW_TRAINING_VIEW_NAME, traineeController.addTraining(userName, model, session));
		verify(traineeService, times(1)).findByUserName(userName, session);
		verify(trainerService, times(1)).findActiveTrainersAssignedOnTrainee(userName, session);
		verify(model).addAttribute(eq(TRAINING), any(TrainingCreateRequestDTO.class));
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
			final var request = new TraineeRequestDTO();
			final var credentials = new CredentialsDTO("User.Name", "Password");
			when(traineeService.save(request)).thenReturn(credentials);

			final String result = traineeController.save(request, bindingResult, redirectAttributes);

			assertEquals(REDIRECT_TO_NEW_TRAINEE, result);
			verify(traineeService, times(1)).save(request);
		}
	}

	@Nested
	class TestHandleActionByUserName {

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsFindAndTraineeExists() {
			final String userName = "UserName";
			final var response = new TraineeResponseDTO();
			response.setUserName(userName);
			when(traineeService.findByUserName(userName, session)).thenReturn(response);

			final String result =
				traineeController.handleActionByUserName("find", userName, model, redirectAttributes, session);

			assertEquals(REDIRECT_TO_TRAINEE_PROFILE + userName, result);
			verify(traineeService, times(1)).findByUserName(userName, session);
			verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsDeleteAndTraineeExists() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName, session)).thenReturn(new TraineeResponseDTO());

			final String result =
				traineeController.handleActionByUserName("delete", userName, model, redirectAttributes, session);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName, session);
			verify(traineeService, times(1)).delete(userName, session);
			verify(redirectAttributes).addFlashAttribute(eq(SUCCESS_MESSAGE_ATTRIBUTE), any(String.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineeIndex_whenTraineeNotFound() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName, session)).thenThrow(new EntityNotFoundException("Not found"));

			final String result =
				traineeController.handleActionByUserName("find", userName, model, redirectAttributes, session);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName, session);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineeIndex_whenActionUnknown() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName, session)).thenReturn(new TraineeResponseDTO());

			final String result =
				traineeController.handleActionByUserName("unknown", userName, model, redirectAttributes, session);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName, session);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}
	}

	@Test
	void edit_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final String userName = "User.Name";
		final var trainee = new TraineeResponseDTO();
		trainee.setUserName(userName);
		when(traineeService.findByUserName(userName, session)).thenReturn(trainee);

		assertEquals(TRAINEE_EDIT_VIEW_NAME, traineeController.edit(userName, model, session));
		verify(traineeService, times(1)).findByUserName(userName, session);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
	}

	@Nested
	class TestUpdate {

		@Test
		void update_shouldReturnNewTraineePage_whenBingingResultHasErrors() {
			when(bindingResult.hasErrors()).thenReturn(true);
			final var trainee = new TraineeRequestDTO();
			final String userName = "User.Name";
			trainee.setUserName(userName);

			final String result = traineeController.update(trainee, bindingResult, session);

			assertEquals(TRAINEE_EDIT_VIEW_NAME, result);
			verifyNoInteractions(traineeService);
		}

		@Test
		void update_shouldSaveTraineeAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final var request = new TraineeRequestDTO();
			final String userName = "User.Name";
			request.setUserName(userName);
			final var trainee = new TraineeRequestDTO();
			trainee.setUserName(userName);

			final String result = traineeController.update(request, bindingResult, session);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).update(trainee, session);
		}
	}

	@Test
	void addTrainer_shouldAddTrainerAndRedirectToTraineePage_whenInvokedWithParams() {
		final String traineeUserName = "Trainee.UserName";
		final String trainerUserName = "Trainer.UserName";
		final var trainee = new TraineeResponseDTO();
		when(traineeService.findByUserName(traineeUserName, session)).thenReturn(trainee);
		final var trainers = new ArrayList<TraineeResponseDTO.TrainerDTO>();
		when(trainerService.findActiveTrainersNotAssignedOnTrainee(traineeUserName, session)).thenReturn(trainers);

		final String result = traineeController.addTrainer(traineeUserName, trainerUserName, model, session);

		assertEquals(REDIRECT_TO_TRAINEE_PROFILE + traineeUserName, result);
		verify(traineeService, times(1)).addTrainer(traineeUserName, trainerUserName, session);
		verify(traineeService, times(1)).findByUserName(traineeUserName, session);
		verify(trainerService, times(1)).findActiveTrainersNotAssignedOnTrainee(traineeUserName, session);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(AVAILABLE_TRAINERS), any(List.class));
	}

	@Test
	void delete_shouldInvokeServiceAndRedirectToTraineeIndexPage_whenInvoked() {
		final String userName = "User.Name";

		assertEquals(REDIRECT_TO_TRAINEE_INDEX, traineeController.delete(userName, session));
		verify(traineeService, times(1)).delete(userName, session);
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainerExists() {
		final String userName = "User.Name";
		final var trainee = new TraineeResponseDTO();
		trainee.setUserName(userName);
		final var trainers = new ArrayList<TraineeResponseDTO.TrainerDTO>();
		when(traineeService.findByUserName(userName, session)).thenReturn(trainee);
		when(trainerService.findActiveTrainersNotAssignedOnTrainee(userName, session)).thenReturn(trainers);

		final String view = traineeController.findByUserName(userName, model, session);

		assertEquals(TRAINEE_VIEW_NAME, view);
		verify(traineeService, times(1)).findByUserName(userName, session);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(AVAILABLE_TRAINERS), any(List.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTraineesExist() {
		final var trainees = List.of(new TraineeResponseDTO());
		when(traineeService.findAll(session)).thenReturn(trainees);

		final String view = traineeController.findAll(model, session);

		assertEquals(TRAINEE_INDEX_VIEW_NAME, view);
		verify(traineeService, times(1)).findAll(session);
		verify(model).addAttribute(eq(TRAINEES), any(List.class));
	}
}
