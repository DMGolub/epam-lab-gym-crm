package com.epam.dmgolub.gym.service.impl;

import com.epam.dmgolub.gym.entity.User;
import com.epam.dmgolub.gym.mapper.EntityToModelMapper;
import com.epam.dmgolub.gym.model.UserModel;
import com.epam.dmgolub.gym.repository.UserRepository;
import com.epam.dmgolub.gym.service.UserService;
import com.epam.dmgolub.gym.service.exception.EntityNotFoundException;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

import static com.epam.dmgolub.gym.interceptor.constant.Constants.TRANSACTION_ID;
import static com.epam.dmgolub.gym.service.constant.Constants.USER_NOT_FOUND_MESSAGE;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserRepository userRepository;
	private final EntityToModelMapper mapper;

	public UserServiceImpl(final UserRepository userRepository, final EntityToModelMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	@Override
	public List<UserModel> findAll() {
		LOGGER.debug("[{}] In findAll - Received a request to find all users", MDC.get(TRANSACTION_ID));
		return mapper.mapToUserModelList(userRepository.findAll());
	}

	@Override
	public void changeActivityStatus(final String userName) {
		LOGGER.debug("[{}] In changeActivityStatus - Received a request to change status for user={}",
			MDC.get(TRANSACTION_ID), userName);
		final var user = getUser(userName);
		user.setActive(!user.isActive());
		LOGGER.debug("[{}] In changeActivityStatus - User {} activity status changed to '{}'",
			MDC.get(TRANSACTION_ID), userName, user.isActive());
		userRepository.saveAndFlush(user);
	}

	private User getUser(final String userName) {
		return userRepository.findByUserName(userName)
			.orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_MESSAGE + userName));
	}
}
