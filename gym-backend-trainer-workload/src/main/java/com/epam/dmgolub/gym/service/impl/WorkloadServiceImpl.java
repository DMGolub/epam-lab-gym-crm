package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.TrainerWorkload;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import com.epam.dmgolub.gym.repository.WorkloadRepository;
import com.epam.dmgolub.gym.service.WorkloadService;
import com.epam.dmgolub.gym.service.exception.WorkloadServiceException;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;

@Service
public class WorkloadServiceImpl implements WorkloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadServiceImpl.class);

	private final WorkloadRepository workloadRepository;

	public WorkloadServiceImpl(final WorkloadRepository workloadRepository) {
		this.workloadRepository = workloadRepository;
	}

	@Override
	public void addWorkload(final WorkloadUpdateRequest trainingWorkload) {
		LOGGER.debug("[{}] In addWorkload - Trying to add training: {}", MDC.get(TRANSACTION_ID), trainingWorkload);

		final var totalTrainerWorkload = findOrCreateTotalTrainerWorkload(trainingWorkload);
		summarize(totalTrainerWorkload, trainingWorkload);
		updateWorkloadTrainerAttributes(totalTrainerWorkload, trainingWorkload);
		workloadRepository.save(totalTrainerWorkload);

		LOGGER.debug("[{}] In addWorkload - Training duration added to workload successfully", MDC.get(TRANSACTION_ID));
	}

	@Override
	public void deleteWorkload(final WorkloadUpdateRequest trainingWorkload) {
		LOGGER.debug("[{}] In deleteWorkload - Trying to delete training: {}", MDC.get(TRANSACTION_ID), trainingWorkload);

		validateTrainingDate(trainingWorkload, "Can't delete workload due to past date");

		final var trainerUserName = trainingWorkload.getTrainerUserName();
		final var totalTrainerWorkload = workloadRepository
			.findById(trainerUserName)
			.orElseThrow(() -> {
				final String message = "Can't delete workload: trainer not found by userName=" + trainerUserName;
				LOGGER.debug("[{}] In deleteWorkload - {}", MDC.get(TRANSACTION_ID), message);
				return new WorkloadServiceException(message);
			});

		subtract(totalTrainerWorkload, trainingWorkload);
		updateWorkloadTrainerAttributes(totalTrainerWorkload, trainingWorkload);
		workloadRepository.save(totalTrainerWorkload);
	}

	private TrainerWorkload findOrCreateTotalTrainerWorkload(final WorkloadUpdateRequest request) {
		final String trainerUserName = request.getTrainerUserName();
		return workloadRepository.findById(trainerUserName).orElseGet(() -> {
			LOGGER.debug("[{}] In getTrainerWorkload - Created new workload due to not found by userName={}",
				MDC.get(TRANSACTION_ID), trainerUserName);
			final var workload = new TrainerWorkload();
			workload.setTrainerUserName(request.getTrainerUserName());
			return workload;
		});
	}

	private void updateWorkloadTrainerAttributes(
		final TrainerWorkload trainerWorkload,
		final WorkloadUpdateRequest request
	) {
		trainerWorkload.setTrainerFirstName(request.getTrainerFirstName());
		trainerWorkload.setTrainerLastName(request.getTrainerLastName());
		trainerWorkload.setTrainerStatus(request.getActive());
	}

	private int getYear(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		return Integer.parseInt(dateFormat.format(date));
	}

	private int getMonth(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
		return Integer.parseInt(dateFormat.format(date));
	}

	private Optional<TrainerWorkload.Year> findYear(final List<TrainerWorkload.Year> years, final int year) {
		return years.stream().filter(y -> y.getValue() == year).findFirst();
	}

	private Optional<TrainerWorkload.Month> findMonth(final List<TrainerWorkload.Month> months, final int month) {
		return months.stream().filter(m -> m.getValue() == month).findFirst();
	}

	private void addDuration(final TrainerWorkload.Month month, final int duration) {
		month.setTrainingSummaryDuration(month.getTrainingSummaryDuration() + duration);
	}

	private void summarize(final TrainerWorkload totalTrainerWorkload, final WorkloadUpdateRequest trainingWorkload) {
		final var year = getYear(trainingWorkload.getDate());
		final var month = getMonth(trainingWorkload.getDate());
		final var workloadYear = findYear(totalTrainerWorkload.getYears(), year);
		if (workloadYear.isPresent()) {
			final var workloadMonths = workloadYear.get().getMonths();
			final var workloadMonth = findMonth(workloadMonths, month);
			if (workloadMonth.isPresent()) {
				addDuration(workloadMonth.get(), trainingWorkload.getDuration());
			} else {
				workloadMonths.add(new TrainerWorkload.Month(month, trainingWorkload.getDuration()));
			}
		} else {
			final var months = List.of(new TrainerWorkload.Month(month, trainingWorkload.getDuration()));
			totalTrainerWorkload.getYears().add(new TrainerWorkload.Year(year, months));
		}
	}

	private void validateTrainingDate(final WorkloadUpdateRequest request, final String message) {
		if (request.getDate().before(new Date())) {
			LOGGER.debug("[{}] In validateTrainingDate - {}", MDC.get(TRANSACTION_ID), message);
			throw new WorkloadServiceException(message);
		}
	}

	private void subtract(final TrainerWorkload totalTrainerWorkload, final WorkloadUpdateRequest trainingWorkload) {
		final String logMessageTemplate = "[{}] In subtract - {}";

		final var workloadYear = findYear(totalTrainerWorkload.getYears(), getYear(trainingWorkload.getDate()));
		if (workloadYear.isEmpty()) {
			final String message = "Can't subtract workload: year not found";
			LOGGER.debug(logMessageTemplate, MDC.get(TRANSACTION_ID), message);
			throw new WorkloadServiceException(message);
		}

		final var month = findMonth(workloadYear.get().getMonths(), getMonth(trainingWorkload.getDate()));
		if (month.isEmpty()) {
			final String message = "Can't subtract workload: month not found";
			LOGGER.debug(logMessageTemplate, MDC.get(TRANSACTION_ID), message);
			throw new WorkloadServiceException(message);
		}

		if (month.get().getTrainingSummaryDuration() < trainingWorkload.getDuration()) {
			final String message = "Can't subtract workload: duration exceeds total workload";
			LOGGER.debug(logMessageTemplate, MDC.get(TRANSACTION_ID), message);
			throw new WorkloadServiceException(message);
		}

		addDuration(month.get(), -trainingWorkload.getDuration());
		LOGGER.debug(logMessageTemplate, MDC.get(TRANSACTION_ID), "Duration subtracted from total workload");
	}
}
