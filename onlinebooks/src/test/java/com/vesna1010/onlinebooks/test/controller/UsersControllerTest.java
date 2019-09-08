package com.vesna1010.onlinebooks.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vesna1010.onlinebooks.controller.UsersController;
import com.vesna1010.onlinebooks.enums.Authority;
import com.vesna1010.onlinebooks.model.User;
import com.vesna1010.onlinebooks.service.UserService;

public class UsersControllerTest extends BaseControllerTest {

	@Mock
	private UserService service;
	@InjectMocks
	@Autowired
	private UsersController controller;
	private Pageable pageable;
	private User user1;
	private User user2;

	{
		pageable = PageRequest.of(0, 10, new Sort(Direction.ASC, "username"));

		user1 = new User("UsernameA", "PasswordA", "emailA@gmail.com", Authority.ADMIN);
		user2 = new User("UsernameB", "PasswordB", "emailB@gmail.com", Authority.USER);
	}

	@Test
	@WithAnonymousUser
	public void findUsersByPageWithAnonymousUserTest() throws Exception {
		when(service.findUsersByPage(pageable)).thenReturn(
				new PageImpl<User>(Arrays.asList(user1, user2), pageable, 2));

		mockMvc.perform(get("/users"))
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findUsersByPage(pageable);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findUsersByPageWithUserTest() throws Exception {
		when(service.findUsersByPage(pageable)).thenReturn(
				new PageImpl<User>(Arrays.asList(user1, user2), pageable, 2));
		
		mockMvc.perform(get("/users"))
		       .andExpect(status().isForbidden());

		verify(service, times(0)).findUsersByPage(pageable);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void findUsersByPageWithAdminTest() throws Exception {
		when(service.findUsersByPage(pageable)).thenReturn(
				new PageImpl<User>(Arrays.asList(user1, user2), pageable, 2));
		
		mockMvc.perform(get("/users"))
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.content", hasSize(2)))
		       .andExpect(jsonPath("$.content[0].username", is("UsernameA")))
		       .andExpect(jsonPath("$.content[1].username", is("UsernameB")))
		       .andExpect(jsonPath("$.totalPages", is(1)))
		       .andExpect(jsonPath("$.number", is(0)))
		       .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findUsersByPage(pageable);
	}

	@Test
	@WithAnonymousUser
	public void userWithAnonymousUserTest() throws Exception {

		mockMvc.perform(get("/users/new"))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void userWithUserTest() throws Exception {

		mockMvc.perform(get("/users/new"))
		       .andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void userWithAdminTest() throws Exception {

		mockMvc.perform(get("/users/new"))
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.username", is(nullValue())))
		       .andExpect(jsonPath("$.authority", is(nullValue())))
		       .andExpect(jsonPath("$.email", is(nullValue())))
		       .andExpect(jsonPath("$.enabled", is(Boolean.TRUE)));
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveUserWithUserTest() throws Exception {
		when(service.saveUser(user1)).thenReturn(user1);

		mockMvc.perform(
				post("/users")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(user1))
				)
		       .andExpect(status().isForbidden());

		verify(service, times(0)).saveUser(user1);
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void saveUserWithAdminTest() throws Exception {
		when(service.saveUser(user1)).thenReturn(user1);

		mockMvc.perform(
				post("/users")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(user1))
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
		       .andExpect(jsonPath("$", is("Saved " + user1)));

		verify(service, times(1)).saveUser(user1);
	}

	@Test
	@WithAnonymousUser
	public void deleteUserByUsernameWithAnonymousUserTest() throws Exception {
		doNothing().when(service).deleteUserByUsername("UsernameA");

		mockMvc.perform(delete("/users/UsernameA"))
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).deleteUserByUsername("UsernameA");
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteUserByUsernameWithUserTest() throws Exception {
		doNothing().when(service).deleteUserByUsername("UsernameA");

		mockMvc.perform(delete("/users/UsernameA"))
		       .andExpect(status().isForbidden());

		verify(service, times(0)).deleteUserByUsername("UsernameA");
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void deleteUserByUsernameWithAdminTest() throws Exception {
		doNothing().when(service).deleteUserByUsername("UsernameA");

		mockMvc.perform(delete("/users/UsernameA"))
		       .andExpect(status().isOk());

		verify(service, times(1)).deleteUserByUsername("UsernameA");
	}

	@Test
	@WithAnonymousUser
	public void disableUserByUsernameWithAnonymousUserTest() throws Exception {
		doNothing().when(service).disableUserByUsername("UsernameA");

		mockMvc.perform(put("/users/UsernameA"))
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).disableUserByUsername("UsernameA");
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void disableUserByUsernameWithUserTest() throws Exception {
		doNothing().when(service).disableUserByUsername("UsernameA");

		mockMvc.perform(put("/users/UsernameA"))
		       .andExpect(status().isForbidden());

		verify(service, times(0)).disableUserByUsername("UsernameA");
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	public void disableUserByUsernameWithAdminTest() throws Exception {
		doNothing().when(service).disableUserByUsername("UsernameA");

		mockMvc.perform(put("/users/UsernameA"))
		       .andExpect(status().isOk());

		verify(service, times(1)).disableUserByUsername("UsernameA");
	}

}
