package com.epam.dmgolub.gym.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ChangePasswordRequestTest {

	private ChangePasswordRequest requestDTO;

	@BeforeEach
	public void setUp() {
		requestDTO = new ChangePasswordRequest();
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
		final ChangePasswordRequest requestDTO2 =
			new ChangePasswordRequest("UserName", "OldPassword", "NewPassword");
		final String expected = "ChangePasswordRequest{userName='UserName'}";

		assertEquals(expected, requestDTO2.toString());
	}

	@Test
	void testEqualsAndHashCode() {
		final ChangePasswordRequest requestDTO2 =
			new ChangePasswordRequest("UserName", "OldPassword", "NewPassword");
		final ChangePasswordRequest requestDTO3 =
			new ChangePasswordRequest("UserName", "OldPassword2", "NewPassword");
		final ChangePasswordRequest requestDTO4 =
			new ChangePasswordRequest("UserName", "OldPassword", "NewPassword2");

		assertEquals(requestDTO, requestDTO);
		assertEquals(requestDTO.hashCode(), requestDTO.hashCode());
		assertNotEquals(requestDTO, requestDTO2);
		assertNotEquals(requestDTO.hashCode(), requestDTO2.hashCode());
		assertNotEquals(null, requestDTO2);
		assertNotEquals(requestDTO2, requestDTO3);
		assertNotEquals(requestDTO2, requestDTO4);
	}
}
