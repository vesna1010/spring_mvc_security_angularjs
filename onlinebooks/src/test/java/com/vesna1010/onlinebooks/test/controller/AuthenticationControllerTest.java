package com.vesna1010.onlinebooks.test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;

public class AuthenticationControllerTest extends BaseControllerTest {

	@Test
	@WithAnonymousUser
	public void findLoggedUserWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/authenticated"))
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=utf-8"))
			   .andExpect(jsonPath("$", is(Boolean.FALSE)));
	}

	@Test
	@WithMockUser(username = "Username", password = "Password", authorities = "USER")
	public void findLoggedUserWithAuthenticatedUserTest() throws Exception {
		
		mockMvc.perform(get("/authenticated"))
	           .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=utf-8"))
		       .andExpect(jsonPath("$", is(Boolean.TRUE)));
	}

	@Test
	@WithAnonymousUser
	public void authoritiesWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/authorities"))
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	@WithAnonymousUser
	public void languagesWithAnonymousUserTest() throws Exception {
		mockMvc.perform(get("/languages"))
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$", hasSize(8)));
	}

}