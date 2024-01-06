package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee, Long> {

	Optional<Trainee> findByUserUserName(String userName);

	void deleteByUserUserName(String userName);
}
