package com.dataagg.security.controller;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dataagg.commons.domain.EUser;
import com.dataagg.security.SecurityApplication;
import com.dataagg.security.UserPrincipal;
import com.dataagg.security.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SecurityApplication.class)
@WebAppConfiguration
public class EUserControllerTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@InjectMocks
	private UserController accountController;

	@Mock
	private UserService userService;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
	}

	@Test
	public void shouldCreateNewUser() throws Exception {

		final EUser user = new EUser();
		user.setUsername("test");
		user.setPassword("password");

		String json = mapper.writeValueAsString(user);

		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	public void shouldFailWhenUserIsNotValid() throws Exception {

		final EUser user = new EUser();
		user.setUsername("t");
		user.setPassword("p");

		mockMvc.perform(post("/users")).andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnCurrentUser() throws Exception {
		mockMvc.perform(get("/users/current").principal(new UserPrincipal("test"))).andExpect(jsonPath("$.name").value("test")).andExpect(status().isOk());
	}
}
