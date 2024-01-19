package com.epam.dmgolub.gym.controller.rest;

import com.epam.dmgolub.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserRestControllerTest {

	private static final String URL = UserRestController.URL + "/profile";

	@Mock
	private UserService userService;
	@InjectMocks
	private UserRestController userRestController;
	private MockMvc mockMvc;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userRestController).build();
	}

	@Test
	void changeActivityStatus_shouldInvokeServiceAndReturnOk_whenPatchRequestIsReceived() throws Exception {
		final String userName = "User.Name";
		mockMvc.perform(patch(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.param("userName", userName))
			.andExpect(status().isOk());

		verify(userService, times(1)).changeActivityStatus(userName);
		verifyNoMoreInteractions(userService);
	}
}
