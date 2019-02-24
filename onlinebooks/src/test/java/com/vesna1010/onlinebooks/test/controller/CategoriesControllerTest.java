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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vesna1010.onlinebooks.controller.CategoriesController;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.service.CategoryService;

public class CategoriesControllerTest extends BaseControllerTest {

	@Mock
	private CategoryService service;
	@InjectMocks
	@Autowired
	private CategoriesController controller;
	private Sort sort;
	private Pageable pageable;
	private Category category1;
	private Category category2;

	{
		Order order1 = new Order(Direction.ASC, "name");
		Order order2 = new Order(Direction.ASC, "id");
		
		sort = Sort.by(order1, order2);
		pageable = PageRequest.of(0, 10, new Sort(Direction.ASC, "id"));
		
		category1 = new Category(1L, "Category A");
		category2 = new Category(2L, "Category B");
	}

	@Test
	@WithAnonymousUser
	public void findAllCategoriesWithAnonymousUserTest() throws Exception {
		when(service.findAllCategories(sort)).thenReturn(Arrays.asList(category1, category2));

		mockMvc.perform(
				get("/categories")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$", hasSize(2)))
			   .andExpect(jsonPath("$[0].name", is("Category A")))
			   .andExpect(jsonPath("$[1].name", is("Category B")));

		verify(service, times(1)).findAllCategories(sort);
	}
	
	@Test
	@WithAnonymousUser
	public void findCategoriesByPageWithAnonymousUserTest() throws Exception {
		when(service.findCategoriesByPage(pageable)).thenReturn(
				new PageImpl<Category>(Arrays.asList(category1, category2), pageable, 2));

		mockMvc.perform(
				get("/categories/page")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findCategoriesByPage(pageable);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void findCategoriesByPageWithAuthenticatedUserTest() throws Exception {
		when(service.findCategoriesByPage(pageable)).thenReturn(
				new PageImpl<Category>(Arrays.asList(category1, category2), pageable, 2));

		mockMvc.perform(
				get("/categories/page")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].name", is("Category A")))
			   .andExpect(jsonPath("$.content[1].name", is("Category B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findCategoriesByPage(pageable);
	}
	
	@Test
	@WithAnonymousUser
	public void categoryWithAnonymousUserTest() throws Exception {

		mockMvc.perform(
				get("/categories/new")
				)
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void categoryWithAuthenticatedUserTest() throws Exception {

		mockMvc.perform(
				get("/categories/new")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.id", is(nullValue())))
			   .andExpect(jsonPath("$.name", is(nullValue())));
	}

	@Test
	@WithAnonymousUser
	public void findCategoryByIdWithAnonymousUserTest() throws Exception {
		when(service.findCategoryById(1L)).thenReturn(category1);

		mockMvc.perform(
				get("/categories/1")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findCategoryById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findCategoryByIdWithAuthenticatedUserTest() throws Exception {
		when(service.findCategoryById(1L)).thenReturn(category1);

		mockMvc.perform(
				get("/categories/1")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.name", is("Category A")));

		verify(service, times(1)).findCategoryById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findCategoryByIdWithExceptionTest() throws Exception {
		when(service.findCategoryById(1L)).thenThrow(new ResourceNotFoundException("No category found with id 1"));

		mockMvc.perform(get("/categories/1"))
		       .andExpect(status().is4xxClientError())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.message", is("No category found with id 1")));

		verify(service, times(1)).findCategoryById(1L);
	}

	@Test
	@WithAnonymousUser
	public void saveCategoryWithAnonymousUserTest() throws Exception {
		when(service.saveCategory(category1)).thenReturn(category1);

		mockMvc.perform(
				post("/categories")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(category1))
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).saveCategory(category1);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveCategoryWithAuthenticatedUserTest() throws Exception {
		when(service.saveCategory(category1)).thenReturn(category1);

		mockMvc.perform(
				post("/categories")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(category1))
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
			   .andExpect(jsonPath("$", is("Saved " + category1)));

		verify(service, times(1)).saveCategory(category1);
	}

	@Test
	@WithAnonymousUser
	public void deleteCategoryByIdWithAnonymousUserTest() throws Exception {
		doNothing().when(service).deleteCategoryById(1L);

		mockMvc.perform(
				delete("/categories/1")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).deleteCategoryById(1L);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteCategoryByIdWithAuthenticatedUserTest() throws Exception {
		doNothing().when(service).deleteCategoryById(1L);

		mockMvc.perform(
				delete("/categories/1")
				)
		       .andExpect(status().isOk());

		verify(service, times(1)).deleteCategoryById(1L);
	}

}