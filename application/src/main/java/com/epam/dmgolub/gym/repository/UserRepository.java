package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	// Empty
}
