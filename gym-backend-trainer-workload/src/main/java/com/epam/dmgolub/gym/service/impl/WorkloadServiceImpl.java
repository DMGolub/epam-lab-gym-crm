package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.TrainerWorkload;
import com.epam.dmgolub.gym.model.WorkloadUpdateRequest;
import com.epam.dmgolub.gym.repository.WorkloadRepository;
import com.epam.dmgolub.gym.service.WorkloadService;
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
		workloadRepository.saveOfUpdate(totalTrainerWorkload);

		LOGGER.debug("[{}] In addWorkload - Training duration added to workload successfully", MDC.get(TRANSACTION_ID));
	}

	@Override
	public boolean deleteWorkload(final WorkloadUpdateRequest trainingWorkload) {
		LOGGER.debug("[{}] In deleteWorkload - Trying to delete training: {}", MDC.get(TRANSACTION_ID), trainingWorkload);

		if (hasPastDate(trainingWorkload)) {
			LOGGER.debug("[{}] In deleteWorkload - Can't delete workload due to past date", MDC.get(TRANSACTION_ID));
			return false;
		}

		final var totalTrainerWorkload = workloadRepository.findByTrainerUserName(trainingWorkload.getTrainerUserName());
		if (totalTrainerWorkload == null) {
			LOGGER.debug("[{}] In deleteWorkload - Can't delete workload: trainer not found", MDC.get(TRANSACTION_ID));
			return false;
		}

		final var isDeleted = subtract(totalTrainerWorkload, trainingWorkload);
		updateWorkloadTrainerAttributes(totalTrainerWorkload, trainingWorkload);
		workloadRepository.saveOfUpdate(totalTrainerWorkload);
		return isDeleted;
	}

	private TrainerWorkload findOrCreateTotalTrainerWorkload(final WorkloadUpdateRequest request) {
		final String trainerUserName = request.getTrainerUserName();
		var workload = workloadRepository.findByTrainerUserName(trainerUserName);
		if (workload == null) {
			LOGGER.debug("[{}] In getTrainerWorkload - Created new workload due to not found by userName={}",
				MDC.get(TRANSACTION_ID), trainerUserName);
			workload = new TrainerWorkload();
			workload.setTrainerUserName(request.getTrainerUserName());
		}
		return workload;
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

	private boolean hasPastDate(final WorkloadUpdateRequest request) {
		return request.getDate().before(new Date());
	}

	private boolean subtract(final TrainerWorkload totalTrainerWorkload, final WorkloadUpdateRequest trainingWorkload) {
		final var workloadYear = findYear(totalTrainerWorkload.getYears(), getYear(trainingWorkload.getDate()));

		if (workloadYear.isEmpty()) {
			LOGGER.debug("[{}] In subtract - Can't subtract workload: year not found", MDC.get(TRANSACTION_ID));
			return false;
		}

		final var month = findMonth(workloadYear.get().getMonths(), getMonth(trainingWorkload.getDate()));
		if (month.isEmpty()) {
			LOGGER.debug("[{}] In subtract - Can't subtract workload: month not found", MDC.get(TRANSACTION_ID));
			return false;
		}

		if (month.get().getTrainingSummaryDuration() >= trainingWorkload.getDuration()) {
			addDuration(month.get(), -trainingWorkload.getDuration());
			LOGGER.debug("[{}] In subtract - Duration subtracted from total workload", MDC.get(TRANSACTION_ID));
			return true;
		}
		LOGGER.debug("[{}] In subtract - Can't subtract workload: duration exceeds total workload", MDC.get(TRANSACTION_ID));
		return false;
	}
}
