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
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vesna1010.onlinebooks.controller.AuthorsController;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.service.AuthorService;

public class AuthorsControllerTest extends BaseControllerTest {

	@Mock
	private AuthorService service;
	@InjectMocks
	@Autowired
	private AuthorsController controller;
	private Sort sort;
	private Pageable pageable;
	private Author author1;
	private Author author2;

	{
		Order order1 = new Order(Direction.ASC, "name");
		Order order2 = new Order(Direction.ASC, "id");
		
		sort = Sort.by(order1, order2);
		pageable = PageRequest.of(0, 10, new Sort(Direction.ASC, "id"));

		author1 = new Author(1L, "Author A", "authorA@gmail.com", 1965);
		author2 = new Author(2L, "Author B", "authorB@gmail.com", 1967);
	}

	@Test
	@WithAnonymousUser
	public void findAllAuthorsWithAnonymousUserTest() throws Exception {
		when(service.findAllAuthors(sort)).thenReturn(Arrays.asList(author1, author2));

		mockMvc.perform(
				get("/authors")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findAllAuthors(sort);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findAllAuthorsWithAuthenticatedUserTest() throws Exception {
		when(service.findAllAuthors(sort)).thenReturn(Arrays.asList(author1, author2));

		mockMvc.perform(
				get("/authors")
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$", hasSize(2)))
		       .andExpect(jsonPath("$[0].name", is("Author A")))
		       .andExpect(jsonPath("$[1].name", is("Author B")));

		verify(service, times(1)).findAllAuthors(sort);
	}

	@WithAnonymousUser
	public void findAuthorsByPageWithAnonymousUserTest() throws Exception {
		when(service.findAuthorsByPage(pageable)).thenReturn(
				new PageImpl<Author>(Arrays.asList(author1, author2), pageable, 2));

		mockMvc.perform(
				get("/authors/page")
				)
			   .andExpect(status().isUnauthorized());

		verify(service, times(0)).findAuthorsByPage(pageable);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findAuthorsByPageWithAuthenticatedUserTest() throws Exception {
		when(service.findAuthorsByPage(pageable)).thenReturn(
				new PageImpl<Author>(Arrays.asList(author1, author2), pageable, 2));

		mockMvc.perform(
				get("/authors/page")
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.content", hasSize(2)))
		       .andExpect(jsonPath("$.content[0].name", is("Author A")))
		       .andExpect(jsonPath("$.content[1].name", is("Author B")))
		       .andExpect(jsonPath("$.totalPages", is(1)))
		       .andExpect(jsonPath("$.number", is(0)))
		       .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findAuthorsByPage(pageable);
	}

	@Test
	@WithAnonymousUser
	public void authorWithAnonymousUserTest() throws Exception {

		mockMvc.perform(
				get("/authors/new")
				)
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void authorWithAuthenticatedUserTest() throws Exception {

		mockMvc.perform(
				get("/authors/new")
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.id", is(nullValue())))
		       .andExpect(jsonPath("$.name", is(nullValue())))
		       .andExpect(jsonPath("$.email", is(nullValue())))
                       .andExpect(jsonPath("$.yearOfBirth", is(nullValue())));
	}

	@Test
	@WithAnonymousUser
	public void findAuthorByIdWithAnonymousUserTest() throws Exception {
		when(service.findAuthorById(1L)).thenReturn(author1);

		mockMvc.perform(
				get("/authors/1")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findAuthorById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findAuthorByIdWithAuthenticatedUserTest() throws Exception {
		when(service.findAuthorById(1L)).thenReturn(author1);

		mockMvc.perform(
				get("/authors/1")
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.name", is("Author A")))
		       .andExpect(jsonPath("$.email", is("authorA@gmail.com")))
		       .andExpect(jsonPath("$.yearOfBirth", is(1965)));

		verify(service, times(1)).findAuthorById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findAuthorByIdWithExceptionTest() throws Exception {
		when(service.findAuthorById(1L)).thenThrow(new ResourceNotFoundException("No author found with id 1"));

		mockMvc.perform(
				get("/authors/1")
				)
		       .andExpect(status().is4xxClientError())
		       .andExpect(content().contentType("application/json;charset=UTF-8"))
		       .andExpect(jsonPath("$.message", is("No author found with id 1")));

		verify(service, times(1)).findAuthorById(1L);
	}

	@Test
	@WithAnonymousUser
	public void saveAuthorWithAnonymousUserTest() throws Exception {
		when(service.saveAuthor(author1)).thenReturn(author1);

		mockMvc.perform(
				post("/authors")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(author1))
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).saveAuthor(author1);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveAuthorWithAuthenticatedUserTest() throws Exception {
		when(service.saveAuthor(author1)).thenReturn(author1);

		mockMvc.perform(
				post("/authors")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(author1))
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
		       .andExpect(jsonPath("$", is("Saved " + author1)));

		verify(service, times(1)).saveAuthor(author1);
	}

	@Test
	@WithAnonymousUser
	public void deleteAuthorByIdWithAnonymousUserTest() throws Exception {
		doNothing().when(service).deleteAuthorById(1L);

		mockMvc.perform(
				delete("/authors/1")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).deleteAuthorById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteAuthorByIdWithAuthenticatedUserTest() throws Exception {
		doNothing().when(service).deleteAuthorById(1L);

		mockMvc.perform(
				delete("/authors/1")
				)
		       .andExpect(status().isOk());

		verify(service, times(1)).deleteAuthorById(1L);
	}

}
