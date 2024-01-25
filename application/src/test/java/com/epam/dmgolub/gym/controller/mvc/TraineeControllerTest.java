package com.epam.dmgolub.gym.controller.mvc;

import com.epam.dmgolub.gym.dto.mvc.TraineeRequestDTO;
import com.epam.dmgolub.gym.dto.mvc.TraineeResponseDTO;
import com.epam.dmgolub.gym.dto.mvc.TrainingRequestDTO;
import com.epam.dmgolub.gym.mapper.mvc.ModelToDtoMapper;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TrainerModel;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.AVAILABLE_TRAINERS;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.ERROR_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.NEW_TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.NEW_TRAINING_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_NEW_TRAINEE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_TRAINEE_INDEX;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.REDIRECT_TO_TRAINEE_PROFILE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.SUCCESS_MESSAGE_ATTRIBUTE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEES;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_EDIT_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_INDEX_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINEE_VIEW_NAME;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINERS;
import static com.epam.dmgolub.gym.controller.mvc.constant.Constants.TRAINING;
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
	@Mock
	private ModelToDtoMapper mapper;
	@InjectMocks
	private TraineeController traineeController;

	@BeforeEach
	void setUp() {
		traineeController = new TraineeController(traineeService, trainerService, mapper);
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
		final var trainee = new TraineeModel();
		when(traineeService.findByUserName(userName)).thenReturn(trainee);
		final var trainers = new ArrayList<TrainerModel>();
		when(trainerService.findActiveTrainersAssignedOnTrainee(userName)).thenReturn(trainers);
		when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(new TraineeResponseDTO());
		when(mapper.mapToTrainerResponseDTOList(trainers)).thenReturn(Collections.emptyList());

		assertEquals(NEW_TRAINING_VIEW_NAME, traineeController.addTraining(userName, model));
		verify(traineeService, times(1)).findByUserName(userName);
		verify(trainerService, times(1)).findActiveTrainersAssignedOnTrainee(userName);
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
			final var request = new TraineeRequestDTO();
			final var trainee = new TraineeModel();
			when(mapper.mapToTraineeModel(request)).thenReturn(trainee);

			final String result = traineeController.save(request, bindingResult, redirectAttributes);

			assertEquals(REDIRECT_TO_NEW_TRAINEE, result);
			verify(mapper, times(1)).mapToTraineeModel(request);
			verify(traineeService, times(1)).save(trainee);
		}
	}

	@Nested
	class TestHandleActionByUserName {

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsFindAndTraineeExists() {
			final String userName = "UserName";
			final var trainee = new TraineeModel();
			final var response = new TraineeResponseDTO();
			response.setUserName(userName);
			when(traineeService.findByUserName(userName)).thenReturn(trainee);
			when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(response);

			final String result =
				traineeController.handleActionByUserName("find", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_PROFILE + userName, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(mapper, times(1)).mapToTraineeResponseDTO(trainee);
			verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		}

		@Test
		void handleActionByUserName_shouldRedirectToTraineePage_whenActionIsDeleteAndTraineeExists() {
			final String userName = "UserName";
			when(traineeService.findByUserName(userName)).thenReturn(new TraineeModel());

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
			when(traineeService.findByUserName(userName)).thenReturn(new TraineeModel());

			final String result =
				traineeController.handleActionByUserName("unknown", userName, model, redirectAttributes);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(traineeService, times(1)).findByUserName(userName);
			verify(redirectAttributes).addFlashAttribute(eq(ERROR_MESSAGE_ATTRIBUTE), any(String.class));
		}
	}

	@Test
	void edit_shouldPopulateModelWithAttributesAndReturnProperViewName_whenInvoked() {
		final String userName = "User.Name";
		final var trainee = new TraineeModel();
		trainee.setUserName(userName);
		when(traineeService.findByUserName(userName)).thenReturn(trainee);
		final var response = new TraineeResponseDTO();
		when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(response);

		assertEquals(TRAINEE_EDIT_VIEW_NAME, traineeController.edit(userName, model));
		verify(traineeService, times(1)).findByUserName(userName);
		verify(mapper, times(1)).mapToTraineeResponseDTO(trainee);
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

			final String result = traineeController.update(trainee, bindingResult);

			assertEquals(TRAINEE_EDIT_VIEW_NAME, result);
			verifyNoInteractions(traineeService);
			verifyNoInteractions(mapper);
		}

		@Test
		void update_shouldSaveTraineeAndRedirectToIndexPage_whenThereAreNoErrors() {
			when(bindingResult.hasErrors()).thenReturn(false);
			final var request = new TraineeRequestDTO();
			final String userName = "User.Name";
			request.setUserName(userName);
			final var trainee = new TraineeModel();
			trainee.setUserName(userName);
			when(mapper.mapToTraineeModel(request)).thenReturn(trainee);

			final String result = traineeController.update(request, bindingResult);

			assertEquals(REDIRECT_TO_TRAINEE_INDEX, result);
			verify(mapper, times(1)).mapToTraineeModel(request);
			verify(traineeService, times(1)).update(trainee);
		}
	}

	@Test
	void addTrainer_shouldAddTrainerAndRedirectToTraineePage_whenInvokedWithParams() {
		final String traineeUserName = "Trainee.UserName";
		final String trainerUserName = "Trainer.UserName";
		final var trainee = new TraineeModel();
		when(traineeService.findByUserName(traineeUserName)).thenReturn(trainee);
		final var trainers = new ArrayList<TrainerModel>();
		when(trainerService.findActiveTrainersNotAssignedOnTrainee(traineeUserName)).thenReturn(trainers);
		when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(new TraineeResponseDTO());
		when(mapper.mapToTrainerResponseDTOList(trainers)).thenReturn(Collections.emptyList());

		final String result = traineeController.addTrainer(traineeUserName, trainerUserName, model);

		assertEquals(REDIRECT_TO_TRAINEE_PROFILE + traineeUserName, result);
		verify(traineeService, times(1)).addTrainer(traineeUserName, trainerUserName);
		verify(traineeService, times(1)).findByUserName(traineeUserName);
		verify(trainerService, times(1)).findActiveTrainersNotAssignedOnTrainee(traineeUserName);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(AVAILABLE_TRAINERS), any(List.class));
	}

	@Test
	void delete_shouldInvokeServiceAndRedirectToTraineeIndexPage_whenInvoked() {
		final String userName = "User.Name";

		assertEquals(REDIRECT_TO_TRAINEE_INDEX, traineeController.delete(userName));
		verify(traineeService, times(1)).delete(userName);
		verifyNoInteractions(mapper);
	}

	@Test
	void findById_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTrainerExists() {
		final String userName = "User.Name";
		final var trainee = new TraineeModel();
		trainee.setUserName(userName);
		final var response = new TraineeResponseDTO();
		response.setUserName(userName);
		final var trainers = new ArrayList<TrainerModel>();
		when(traineeService.findByUserName(userName)).thenReturn(trainee);
		when(trainerService.findActiveTrainersNotAssignedOnTrainee(userName)).thenReturn(trainers);
		when(mapper.mapToTraineeResponseDTO(trainee)).thenReturn(response);
		when(mapper.mapToTrainerResponseDTOList(trainers)).thenReturn(Collections.emptyList());

		final String view = traineeController.findByUserName(userName, model);

		assertEquals(TRAINEE_VIEW_NAME, view);
		verify(traineeService, times(1)).findByUserName(userName);
		verify(mapper, times(1)).mapToTraineeResponseDTO(trainee);
		verify(mapper, times(1)).mapToTrainerResponseDTOList(trainers);
		verify(model).addAttribute(eq(TRAINEE), any(TraineeResponseDTO.class));
		verify(model).addAttribute(eq(AVAILABLE_TRAINERS), any(List.class));
	}

	@Test
	void findAll_shouldPopulateModelWithAttributeAndReturnProperViewName_whenTraineesExist() {
		final var trainees = List.of(new TraineeModel());
		when(traineeService.findAll()).thenReturn(trainees);
		final var response = List.of(new TraineeResponseDTO());
		when(mapper.mapToTraineeResponseDTOList(trainees)).thenReturn(response);

		final String view = traineeController.findAll(model);

		assertEquals(TRAINEE_INDEX_VIEW_NAME, view);
		verify(traineeService, times(1)).findAll();
		verify(mapper, times(1)).mapToTraineeResponseDTOList(trainees);
		verify(model).addAttribute(eq(TRAINEES), any(List.class));
	}
}
