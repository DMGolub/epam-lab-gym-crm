package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.dto.TrainerWorkloadUpdateRequestDTO;
import com.epam.dmgolub.gym.model.TraineeModel;
import com.epam.dmgolub.gym.model.TrainerModel;
import com.epam.dmgolub.gym.model.TrainingModel;
import com.epam.dmgolub.gym.model.TrainingTypeModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JmsWorkloadUpdateServiceImplTest {

	@Mock
	private JmsTemplate jmsTemplate;
	@InjectMocks
	private JmsWorkloadUpdateServiceImpl jmsWorkloadUpdateService;

	private final TrainingModel training;

	{
		final var type = new TrainingTypeModel(1L, "Bodybuilding");
		training = new TrainingModel(1L, new TraineeModel(), new TrainerModel(), "Training", type, new Date(), 90);
	}

	@Test
	void add_shouldConvertAndSendRequest_whenInvoked() {
		jmsWorkloadUpdateService.add(training);

		verify(jmsTemplate).convertAndSend(any(String.class), any(TrainerWorkloadUpdateRequestDTO.class));
	}

	@Test
	void delete_shouldConvertAndSendRequest_whenInvoked() {
		jmsWorkloadUpdateService.delete(training);

		verify(jmsTemplate).convertAndSend(any(String.class), any(TrainerWorkloadUpdateRequestDTO.class));
	}
}
