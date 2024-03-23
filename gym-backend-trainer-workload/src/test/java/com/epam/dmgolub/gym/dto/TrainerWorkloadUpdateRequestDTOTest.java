package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainerWorkloadUpdateRequestDTOTest {

	private TrainerWorkloadUpdateRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new TrainerWorkloadUpdateRequestDTO();
	}

	@Test
	void trainerUserName_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "User.Name";
		requestDTO.setTrainerUserName(userName);
		assertEquals(userName, requestDTO.getTrainerUserName());
	}

	@Test
	void trainerFirstNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String firstName = "FirstName";
		requestDTO.setTrainerFirstName(firstName);
		assertEquals(firstName, requestDTO.getTrainerFirstName());
	}

	@Test
	void trainerLastNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String lastName = "LastName";
		requestDTO.setTrainerLastName(lastName);
		assertEquals(lastName, requestDTO.getTrainerLastName());
	}

	@Test
	void isActiveGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		requestDTO.setActive(true);
		assertTrue(requestDTO.getActive());
	}

	@Test
	void dateGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final Date date = new Date();
		requestDTO.setDate(date);
		assertEquals(date, requestDTO.getDate());
	}

	@Test
	void durationGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final int duration = 90;
		requestDTO.setDuration(duration);
		assertEquals(duration, requestDTO.getDuration());
	}

	@Test
	void actionTypeGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String actionType = "ADD";
		requestDTO.setActionType(actionType);
		assertEquals(actionType, requestDTO.getActionType());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		requestDTO = new TrainerWorkloadUpdateRequestDTO(
			"User.Name",
			"FirstName",
			"LastName",
			true,
			null,
			60,
			"DELETE"
		);
		final var expected = "TrainingRequestDTO{trainerUserName='User.Name', trainerFirstName='FirstName', " +
			"trainerLastName='LastName', isActive=true, date=null, duration=60, actionType='DELETE'}";
		assertEquals(expected, requestDTO.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		requestDTO =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", true, null, 60, "DELETE");
		final var requestDTO2 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", true, null, 60, "DELETE");
		final var requestDTO3 =
			new TrainerWorkloadUpdateRequestDTO("User.Name2", "FirstName", "LastName", true, null, 60, "DELETE");
		final var requestDTO4 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName2", "LastName", true, null, 60, "DELETE");
		final var requestDTO5 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName2", true, null, 60, "DELETE");
		final var requestDTO6 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", false, null, 60, "DELETE");
		final var requestDTO7 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", true, new Date(), 60, "DELETE");
		final var requestDTO8 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", true, null, 90, "DELETE");
		final var requestDTO9 =
			new TrainerWorkloadUpdateRequestDTO("User.Name", "FirstName", "LastName", true, null, 60, "ADD");

		assertEquals(requestDTO, requestDTO2);
		assertEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2.hashCode(), requestDTO3.hashCode());
		assertNotEquals(requestDTO2, requestDTO4);
		assertNotEquals(requestDTO2.hashCode(), requestDTO4.hashCode());
		assertNotEquals(requestDTO2, requestDTO5);
		assertNotEquals(requestDTO2.hashCode(), requestDTO5.hashCode());
		assertNotEquals(requestDTO2, requestDTO6);
		assertNotEquals(requestDTO2.hashCode(), requestDTO6.hashCode());
		assertNotEquals(requestDTO2, requestDTO7);
		assertNotEquals(requestDTO2.hashCode(), requestDTO7.hashCode());
		assertNotEquals(requestDTO2, requestDTO8);
		assertNotEquals(requestDTO2.hashCode(), requestDTO8.hashCode());
		assertNotEquals(requestDTO2, requestDTO9);
		assertNotEquals(requestDTO2.hashCode(), requestDTO9.hashCode());
	}
}
