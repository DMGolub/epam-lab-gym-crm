package com.epam.dmgolub.gym.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ChangePasswordRequestDTOTest {

	private ChangePasswordRequestDTO requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new ChangePasswordRequestDTO();
	}

	@Test
	void userNameGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String userName = "UserName";
		requestDTO.setUserName(userName);
		assertEquals(userName, requestDTO.getUserName());
	}

	@Test
	void oldPasswordGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String password = "OldPassword";
		requestDTO.setOldPassword(password);
		assertEquals(password, requestDTO.getOldPassword());
	}

	@Test
	void newPasswordGetter_shouldReturnTheSameValue_whenItIsSetBySetter() {
		final String password = "NewPassword";
		requestDTO.setNewPassword(password);
		assertEquals(password, requestDTO.getNewPassword());
	}

	@Test
	void toString_shouldReturnExpectedValue_whenInvoked() {
		final ChangePasswordRequestDTO requestDTO2 =
			new ChangePasswordRequestDTO("UserName", "OldPassword", "NewPassword");
		final String expected = "ChangePasswordRequestDTO{userName='UserName'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final ChangePasswordRequestDTO requestDTO2 =
			new ChangePasswordRequestDTO("UserName", "OldPassword", "NewPassword");
		final ChangePasswordRequestDTO requestDTO3 =
			new ChangePasswordRequestDTO("UserName", "OldPassword2", "NewPassword");
		final ChangePasswordRequestDTO requestDTO4 =
			new ChangePasswordRequestDTO("UserName", "OldPassword", "NewPassword2");

		assertEquals(requestDTO, requestDTO);
		assertEquals(requestDTO.hashCode(), requestDTO.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
	}
}
