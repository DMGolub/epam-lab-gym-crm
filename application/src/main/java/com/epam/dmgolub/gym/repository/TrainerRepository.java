package com.epam.dmgolub.gym.repository;

import com.epam.dmgolub.gym.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {

	Optional<Trainer> findByUserUserName(String userName);
}
