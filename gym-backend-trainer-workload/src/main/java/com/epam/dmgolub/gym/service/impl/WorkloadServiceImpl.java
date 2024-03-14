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
	public void addTraining(final WorkloadUpdateRequest request) {
		LOGGER.debug("[{}] In addTraining - Received a request to add training duration to workload: {}",
			MDC.get(TRANSACTION_ID), request);
		final var workload = getOrCreateTrainerWorkload(request);
		updateTrainerWorkloadTrainerAttributes(workload, request);
		final var trainingYear = getYearFromDate(request.getDate());
		final var trainingMonth = getMonthFromDate(request.getDate());
		final var year = findYear(workload.getYears(), trainingYear);
		if (year.isPresent()) {
			final var workloadMonths = year.get().getMonths();
			final var workloadMonth = findMonth(workloadMonths, trainingMonth);
			if (workloadMonth.isPresent()) {
				addDuration(workloadMonth.get(), request.getDuration());
			} else {
				workloadMonths.add(new TrainerWorkload.Month(trainingMonth, request.getDuration()));
			}
		} else {
			workload.getYears().add(new TrainerWorkload.Year(
				trainingYear,
				List.of(new TrainerWorkload.Month(trainingMonth, request.getDuration()))
			));
		}
		workloadRepository.saveOfUpdate(workload);
		LOGGER.debug("[{}] In addTraining - Training duration added to workload successfully", MDC.get(TRANSACTION_ID));
	}

	@Override
	public boolean deleteTraining(final WorkloadUpdateRequest request) {
		LOGGER.debug("[{}] In deleteTraining - Received request to subtract training duration: {}",
			MDC.get(TRANSACTION_ID), request);

		if (request.getDate().before(new Date())) {
			LOGGER.debug("[{}] In deleteTraining - Can not subtract duration from total workload due to past date",
				MDC.get(TRANSACTION_ID));
			return false;
		}

		final var workload = workloadRepository.findByTrainerUserName(request.getTrainerUserName());
		if (workload != null) {
			final var year = findYear(workload.getYears(), getYearFromDate(request.getDate()));
			if (year.isPresent()) {
				final var months = year.get().getMonths();
				final var month = findMonth(months, getMonthFromDate(request.getDate()));
				if (month.isPresent() && month.get().getTrainingSummaryDuration() >= request.getDuration()) {
					addDuration(month.get(), -request.getDuration());
					updateTrainerWorkloadTrainerAttributes(workload, request);
					workloadRepository.saveOfUpdate(workload);
					LOGGER.debug("[{}] In addTraining - Training duration subtracted from total workload",
						MDC.get(TRANSACTION_ID));
					return true;
				}
			}
			LOGGER.debug("[{}] In deleteTraining - Can not subtract duration from total workload: date not found",
				MDC.get(TRANSACTION_ID));
			return false;
		}
		LOGGER.debug("[{}] In deleteTraining - Can not subtract duration from total workload: userName not found",
			MDC.get(TRANSACTION_ID));
		return false;
	}

	private TrainerWorkload getOrCreateTrainerWorkload(final WorkloadUpdateRequest request) {
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

	private void updateTrainerWorkloadTrainerAttributes(
		final TrainerWorkload trainerWorkload,
		final WorkloadUpdateRequest request
	) {
		trainerWorkload.setTrainerFirstName(request.getTrainerFirstName());
		trainerWorkload.setTrainerLastName(request.getTrainerLastName());
		trainerWorkload.setTrainerStatus(request.getActive());
	}

	private int getYearFromDate(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		return Integer.parseInt(dateFormat.format(date));
	}

	private int getMonthFromDate(final Date date) {
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
}
