package com.epam.dmgolub.gym.listener;

import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.mapper.DtoToModelMapper;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import com.epam.dmgolub.gym.service.WorkloadService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadUpdateRequestListenerTest {

	private static final String DLQ_NAME = "ActiveMQ.DLQ";

	@Mock
	private JmsTemplate jmsTemplate;
	@Mock
	private WorkloadService workloadService;
	@Mock
	private DtoToModelMapper mapper;
	private TrainerWorkloadUpdateRequestListener listener;

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final String userName = "FirstName.LastName";
	private final String firstName = "FirstName";
	private final String lastName = "LastName";
	private final boolean isActive = true;
	private final int duration = 90;

	@BeforeEach
	void setUp() {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		listener = new TrainerWorkloadUpdateRequestListener(jmsTemplate, validator, workloadService, mapper);
	}

	@Nested
	class TestAdd {

		@Test
		void receiveRequest_shouldInvokeAddTraining_whenActionTypeIsAddAndRequestIsValid() throws Exception {
			final var date = dateFormat.parse("2025-04-05");
			final var requestDTO =
				new TrainerWorkloadUpdateRequestDTO(userName, firstName, lastName, isActive, date, duration, "ADD");
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			when(mapper.mapToWorkloadUpdateRequest(requestDTO)).thenReturn(request);

			listener.receiveRequest(requestDTO);

			verify(mapper).mapToWorkloadUpdateRequest(requestDTO);
			verify(workloadService).addWorkload(request);
			verifyNoMoreInteractions(workloadService);
		}

		@Test
		void receiveRequest_shouldSendMessageToDLQ_whenActionTypeIsAddAndRequestIsNotValid() throws Exception {
			final var date = dateFormat.parse("2025-04-05");
			final var requestDTO =
				new TrainerWorkloadUpdateRequestDTO(userName, null, null, isActive, date, duration, "ADD");

			listener.receiveRequest(requestDTO);

			verify(jmsTemplate).convertAndSend(DLQ_NAME, requestDTO);
			verifyNoInteractions(mapper);
			verifyNoInteractions(workloadService);
		}
	}

	@Nested
	class TestDelete {

		@Test
		void receiveRequest_shouldInvokeDeleteTraining_whenActionTypeIsDeleteAndRequestIsValid() throws Exception {
			final var date = dateFormat.parse("2025-04-05");
			final var requestDTO =
				new TrainerWorkloadUpdateRequestDTO(userName, firstName, lastName, isActive, date, duration, "DELETE");
			final var request = new WorkloadUpdateRequest(userName, firstName, lastName, isActive, date, duration);
			when(mapper.mapToWorkloadUpdateRequest(requestDTO)).thenReturn(request);

			listener.receiveRequest(requestDTO);

			verify(mapper).mapToWorkloadUpdateRequest(requestDTO);
			verify(workloadService).deleteWorkload(request);
			verifyNoMoreInteractions(workloadService);
		}

		@Test
		void receiveRequest_shouldSendMessageToDLQ_whenActionTypeIsDeleteAndRequestIsNotValid() throws Exception {
			final var date = dateFormat.parse("2025-04-05");
			final var requestDTO =
				new TrainerWorkloadUpdateRequestDTO(userName, null, null, isActive, date, duration, "DELETE");

			listener.receiveRequest(requestDTO);

			verify(jmsTemplate).convertAndSend(DLQ_NAME, requestDTO);
			verifyNoInteractions(mapper);
			verifyNoInteractions(workloadService);
		}
	}
}
