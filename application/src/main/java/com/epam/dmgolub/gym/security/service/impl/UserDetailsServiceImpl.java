package com.epam.dmgolub.gym.security.service.impl;

import com.epam.dmgolub.gym.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		LOGGER.debug("In loadUserByUsername - Fetching user with userName={} from repository", username);
		final var user = userRepository.findByUserName(username)
			.orElseThrow(() -> new UsernameNotFoundException(username));
		LOGGER.debug("In loadUserByUsername - User with userName={} fetched successfully", username);
		return new User(user.getUserName(), user.getPassword(), Collections.emptyList());
	}
}
