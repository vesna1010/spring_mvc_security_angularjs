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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
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
import com.vesna1010.onlinebooks.controller.BooksController;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.service.BookService;

public class BooksControllerTest extends BaseControllerTest {

	@Mock
	private BookService service;
	@InjectMocks
	@Autowired
	private BooksController controller;
	private Pageable pageable;
	private Pageable pageable1;
	private Book book1;
	private Book book2;
	private Category category;
	private Author author;

	{
		Order order1 = new Order(Direction.DESC, "date");
		Order order2 = new Order(Direction.ASC, "isbn");
		Sort sort = Sort.by(order1, order2);
		
		pageable = PageRequest.of(0, 10, sort);
		pageable1 = PageRequest.of(0, 10, new Sort(Direction.ASC, "isbn"));
		
		author = new Author(1L, "Author", "author@gmail.com", 1965);
		category = new Category(1L, "Category");
		book1 = new Book("1234567890121", "Title A", Language.ENGLISH, category, getContents(), LocalDate.of(2018, 7, 2));
		book2 = new Book("1234567890122", "Title B", Language.ENGLISH, category, getContents(), LocalDate.of(2018, 7, 1));
		
		book1.setAuthors(new HashSet<Author>(Arrays.asList(author)));
		book2.setAuthors(new HashSet<Author>(Arrays.asList(author)));
		
		book1.setContents(getContents());
		book2.setContents(getContents());
	}

	@Test
	@WithAnonymousUser
	public void findBooksTest() throws Exception {
		when(service.findBooksByPage(pageable)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable, 2));
		
		mockMvc.perform(
				get("/books")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByPage(pageable);
	}

	@Test
	@WithAnonymousUser
	public void findBooksByTitleAndPageTest() throws Exception {	
		when(service.findBooksByTitleAndPage("Title", pageable)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable, 2));

		mockMvc.perform(
				get("/books/title/Title")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByTitleAndPage("Title", pageable);
	}

	@Test
	@WithAnonymousUser
	public void findBooksByAuthorNameAndPageTest() throws Exception {	
		when(service.findBooksByAuthorNameAndPage("Author", pageable)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable, 2));
	
		mockMvc.perform(
				get("/books/author/Author")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByAuthorNameAndPage("Author", pageable);
	}

	@Test
	@WithAnonymousUser
	public void findBooksByCategoryIdAndPageTest() throws Exception {	
		when(service.findBooksByCategoryIdAndPage(1L, pageable)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable, 2));

		mockMvc.perform(
				get("/books/category/1")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByCategoryIdAndPage(1L, pageable);
	}
	
	@Test
	@WithAnonymousUser
	public void findBooksByLanguageAndPageTest() throws Exception {	
		when(service.findBooksByLanguageAndPage(Language.ENGLISH, pageable)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable, 2));
	
		mockMvc.perform(
				get("/books/language/ENGLISH")
				)
			   .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByLanguageAndPage(Language.ENGLISH, pageable);
	}
	
	@Test
	@WithAnonymousUser
	public void findBooksByPageWithAnonymousUserTest() throws Exception {
		when(service.findBooksByPage(pageable1)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable1, 2));
		
		mockMvc.perform(
				get("/books/page")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findBooksByPage(pageable);
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void findBooksByPageWithAuthenticatedUserTest() throws Exception {
		when(service.findBooksByPage(pageable1)).thenReturn(
				new PageImpl<Book>(Arrays.asList(book1, book2), pageable1, 2));
		
		mockMvc.perform(
				get("/books/page")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.content", hasSize(2)))
			   .andExpect(jsonPath("$.content[0].title", is("Title A")))
			   .andExpect(jsonPath("$.content[1].title", is("Title B")))
			   .andExpect(jsonPath("$.totalPages", is(1)))
			   .andExpect(jsonPath("$.number", is(0)))
			   .andExpect(jsonPath("$.size", is(10)));

		verify(service, times(1)).findBooksByPage(pageable1);
	}
	
	@Test
	@WithAnonymousUser
	public void bookWithAnonymousUserTest() throws Exception {
		
		mockMvc.perform(
				get("/books/new")
				)
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void bookWithAuthenticatedUserTest() throws Exception {
		
		mockMvc.perform(
				get("/books/new")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.title", is(nullValue())))
			   .andExpect(jsonPath("$.language", is(nullValue())))
			   .andExpect(jsonPath("$.category", is(nullValue())))
			   .andExpect(jsonPath("$.authors", hasSize(0)));
	}
	
	@Test
	@WithAnonymousUser
	public void findBookByIsbnWithAnonymousUserTest() throws Exception {
		when(service.findBookByIsbn("1234567890121")).thenReturn(book1);

		mockMvc.perform(
				get("/books/1234567890121")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).findBookByIsbn("1234567890121");
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void findBookByIsbnWithAuthenticatedUserTest() throws Exception {
		when(service.findBookByIsbn("1234567890121")).thenReturn(book1);

		mockMvc.perform(
				get("/books/1234567890121")
				)
		       .andExpect(status().isOk())
			   .andExpect(content().contentType("application/json;charset=UTF-8"))
			   .andExpect(jsonPath("$.title", is("Title A")))
			   .andExpect(jsonPath("$.language", is("ENGLISH")))
			   .andExpect(jsonPath("$.category.name", is("Category")))
			   .andExpect(jsonPath("$.authors", hasSize(1)));

		verify(service, times(1)).findBookByIsbn("1234567890121");
	}
	
	@Test
	@WithMockUser(authorities = "USER")
	public void findBookByIsbnWithExceptionTest() throws Exception {
		when(service.findBookByIsbn("1234567890121")).thenThrow(
				new ResourceNotFoundException("No book found with isbn 1234567890121"));
		
		mockMvc.perform(
				get("/books/1234567890121")
				)
		       .andExpect(status().is4xxClientError())
	           .andExpect(content().contentType("application/json;charset=UTF-8"))
	           .andExpect(jsonPath("$.message", is("No book found with isbn 1234567890121")));

		verify(service, times(1)).findBookByIsbn("1234567890121");
	}
	
	@Test
	@WithAnonymousUser
	public void saveBookWithAnonymousUserTest() throws Exception {
		when(service.saveBook(book1)).thenReturn(book1);

		mockMvc.perform(
				post("/books")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(book1))
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).saveBook(book1);
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void saveBookWithAuthenticatedUserTest() throws Exception {
		when(service.saveBook(book1)).thenReturn(book1);

		mockMvc.perform(
				post("/books")
				.contentType("application/json;charset=UTF-8")
				.content(new ObjectMapper().writeValueAsString(book1))
				)
		       .andExpect(status().isOk())
		       .andExpect(content().contentType("text/plain;charset=ISO-8859-1"))
		       .andExpect(jsonPath("$", is("Saved " + book1)));

		verify(service, times(1)).saveBook(book1);
	}
	
	@Test
	@WithAnonymousUser
	public void downloadBookByIsbnWithAnonymousUserTest() throws Exception {
		when(service.findBookByIsbn("1234567890121")).thenReturn(book1);
		
		mockMvc.perform(
				get("/books/download/1234567890121")
				)
	           .andExpect(status().isOk())
		       .andExpect(content().contentType("application/octet-stream"));

		verify(service, times(1)).findBookByIsbn("1234567890121");
	}
	
	@Test
	@WithAnonymousUser
	public void deleteBookByIdWithAnonymousUserTest() throws Exception {
		doNothing().when(service).deleteBookByIsbn("1234567890121");

		mockMvc.perform(
				delete("/books/1234567890121")
				)
		       .andExpect(status().isUnauthorized());

		verify(service, times(0)).deleteBookByIsbn("1234567890121");
	}

	@Test
	@WithMockUser(authorities = "USER")
	public void deleteBookByIdWithAuthenticatedUserTest() throws Exception {
		doNothing().when(service).deleteBookByIsbn("1234567890121");

		mockMvc.perform(
				delete("/books/1234567890121")
				)
		       .andExpect(status().isOk());

		verify(service, times(1)).deleteBookByIsbn("1234567890121");
	}
	
}