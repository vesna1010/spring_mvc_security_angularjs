package com.vesna1010.onlinebooks.test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.AuthorRepository;
import com.vesna1010.onlinebooks.repository.BookRepository;
import com.vesna1010.onlinebooks.service.impl.AuthorServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class AuthorServiceTest {

	@Mock
	private AuthorRepository authorRepository;
	@Mock
	private BookRepository bookRepository;
	@InjectMocks
	private AuthorServiceImpl service;
	private Sort sort;
	private Pageable pageable;
	private Author author1;
	private Author author2;
	private Book book1;
	private Book book2;

	@Before
	public void setUp() {
		sort = new Sort(Direction.ASC, "id");
		pageable = PageRequest.of(0, 10, sort);

		author1 = new Author(1L, "Author A", "authorA@gmail.com", 1965);
		author2 = new Author(2L, "Author B", "authorB@gmail.com", 1967);

		book1 = new Book("1234567890121", "Title A", Language.ENGLISH, new Category(1L, "Category"), new byte[0],
				LocalDate.now());
		book2 = new Book("1234567890122", "Title B", Language.ENGLISH, new Category(1L, "Category"), new byte[0],
				LocalDate.now());

		book1.addAuthor(author1);
		book1.addAuthor(author2);
		book2.addAuthor(author1);
	}

	@Test
	public void findAllAuthorsTest() {
		when(authorRepository.findAll(sort)).thenReturn(Arrays.asList(author1, author2));

		List<Author> authors = service.findAllAuthors(sort);

		assertThat(authors, hasSize(2));
		assertThat(authors.get(0), is(author1));
		assertThat(authors.get(1), is(author2));
		verify(authorRepository, times(1)).findAll(sort);
	}

	@Test
	public void findAuthorsByPageTest() {
		when(authorRepository.findAll(pageable)).thenReturn(new PageImpl<Author>(Arrays.asList(author1, author2)));

		Page<Author> page = service.findAuthorsByPage(pageable);
		List<Author> authors = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(authors, hasSize(2));
		assertThat(authors.get(0), is(author1));
		assertThat(authors.get(1), is(author2));
		verify(authorRepository, times(1)).findAll(pageable);
	}

	@Test
	public void findAuthorByIdTest() {
		when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));

		Author author = service.findAuthorById(1L);

		assertThat(author.getName(), is("Author A"));
		assertThat(author.getEmail(), is("authorA@gmail.com"));
		assertThat(author.getYearOfBirth(), is(1965));
		verify(authorRepository, times(1)).findById(1L);
	}

	@Test(expected = ResourceNotFoundException.class)
	public void authorNotFoundTest() {
		when(authorRepository.findById(3L)).thenReturn(Optional.empty());

		service.findAuthorById(3L);
	}

	@Test
	public void saveAuthorTest() {
		when(authorRepository.save(author1)).thenReturn(author1);

		Author author = service.saveAuthor(author1);

		assertThat(author.getId(), is(1L));
		assertThat(author.getName(), is("Author A"));
		assertThat(author.getEmail(), is("authorA@gmail.com"));
		assertThat(author.getYearOfBirth(), is(1965));
		verify(authorRepository, times(1)).save(author1);
	}

	@Test
	public void deleteAuthorTest() {
		when(authorRepository.findById(1L)).thenReturn(Optional.of(author1));
		doNothing().when(authorRepository).deleteById(1L);
		doNothing().when(bookRepository).deleteByIsbn("1234567890121");
		doNothing().when(bookRepository).deleteByIsbn("1234567890122");
		when(bookRepository.save(book1)).thenReturn(book1);
		when(bookRepository.save(book2)).thenReturn(book2);

		service.deleteAuthorById(1L);

		assertThat(book1.getAuthors(), hasSize(1));
		verify(authorRepository, times(1)).findById(1L);
		verify(authorRepository, times(1)).deleteById(1L);
		verify(bookRepository, times(0)).deleteByIsbn("1234567890121");
		verify(bookRepository, times(1)).deleteByIsbn("1234567890122");
		verify(bookRepository, times(1)).save(book1);
		verify(bookRepository, times(0)).save(book2);
	}

}