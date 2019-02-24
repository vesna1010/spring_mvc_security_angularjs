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
import org.springframework.data.domain.Sort.Order;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.BookRepository;
import com.vesna1010.onlinebooks.service.impl.BookServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

	@Mock
	private BookRepository repository;
	@InjectMocks
	private BookServiceImpl service;
	private Pageable pageable;
	private Book book1;
	private Book book2;
	private Category category;
	private Author author;

	@Before
	public void setUp() {
		Order order1 = new Order(Direction.DESC, "date");
		Order order2 = new Order(Direction.ASC, "isbn");
		Sort sort = Sort.by(order1, order2);

		pageable = PageRequest.of(0, 10, sort);

		author = new Author("Name", "author1@gmail.com", 1965);
		category = new Category(1L, "Category");

		book1 = new Book("1234567890121", "Title A", Language.ENGLISH, category, new byte[0], LocalDate.of(2018, 7, 1));
		book2 = new Book("1234567890122", "Title B", Language.ENGLISH, category, new byte[0], LocalDate.of(2018, 7, 1));

		book1.addAuthor(author);
		book2.addAuthor(author);
	}

	@Test
	public void findBooksByPageTest() {
		when(repository.findAll(pageable)).thenReturn(new PageImpl<Book>(Arrays.asList(book1, book2)));

		Page<Book> page = service.findBooksByPage(pageable);
		List<Book> books = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(books, hasSize(2));
		assertThat(books.get(0), is(book1));
		assertThat(books.get(1), is(book2));
		verify(repository, times(1)).findAll(pageable);
	}

	@Test
	public void findBooksByTitleAndPageTest() {
		when(repository.findAllByTitleContains("Title", pageable))
				.thenReturn(new PageImpl<Book>(Arrays.asList(book1, book2)));

		Page<Book> page = service.findBooksByTitleAndPage("Title", pageable);
		List<Book> books = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(books, hasSize(2));
		assertThat(books.get(0), is(book1));
		assertThat(books.get(1), is(book2));
		verify(repository, times(1)).findAllByTitleContains("Title", pageable);
	}

	@Test
	public void findBooksByAuthorNameAndPageTest() {
		when(repository.findAllDistinctByAuthorsNameContains("Name", pageable))
				.thenReturn(new PageImpl<Book>(Arrays.asList(book1, book2)));

		Page<Book> page = service.findBooksByAuthorNameAndPage("Name", pageable);
		List<Book> books = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(books, hasSize(2));
		assertThat(books.get(0), is(book1));
		assertThat(books.get(1), is(book2));
		verify(repository, times(1)).findAllDistinctByAuthorsNameContains("Name", pageable);
	}

	@Test
	public void findBooksByCategoryAndPageTest() {
		when(repository.findAllByCategoryId(1L, pageable)).thenReturn(new PageImpl<Book>(Arrays.asList(book1, book2)));

		Page<Book> page = service.findBooksByCategoryIdAndPage(1L, pageable);
		List<Book> books = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(books, hasSize(2));
		assertThat(books.get(0), is(book1));
		assertThat(books.get(1), is(book2));
		verify(repository, times(1)).findAllByCategoryId(1L, pageable);
	}

	@Test
	public void findBooksByLanguageAndPageTest() {
		when(repository.findAllByLanguage(Language.ENGLISH, pageable))
				.thenReturn(new PageImpl<Book>(Arrays.asList(book1, book2)));

		Page<Book> page = service.findBooksByLanguageAndPage(Language.ENGLISH, pageable);
		List<Book> books = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(books, hasSize(2));
		assertThat(books.get(0), is(book1));
		assertThat(books.get(1), is(book2));
		verify(repository, times(1)).findAllByLanguage(Language.ENGLISH, pageable);
	}

	@Test
	public void findBookByIsbnTest() {
		when(repository.findByIsbn("1234567890121")).thenReturn(Optional.of(book1));

		Book book = service.findBookByIsbn("1234567890121");

		assertThat(book.getTitle(), is("Title A"));
		assertThat(book.getLanguage(), is(Language.ENGLISH));
		assertThat(book.getCategory(), is(category));
		assertThat(book.getAuthors(), hasSize(1));
		verify(repository, times(1)).findByIsbn("1234567890121");
	}

	@Test(expected = ResourceNotFoundException.class)
	public void findBookByIsbnWhenBookNotExistsTest() {
		when(repository.findByIsbn("1234567890123")).thenReturn(Optional.empty());

		service.findBookByIsbn("1234567890123");
	}

	@Test
	public void saveBookTest() {
		when(repository.save(book1)).thenReturn(book1);

		Book book = service.saveBook(book1);

		assertThat(book.getIsbn(), is("1234567890121"));
		assertThat(book.getTitle(), is("Title A"));
		verify(repository, times(1)).save(book1);
	}

	@Test
	public void deleteBookByIsbnTest() {
		doNothing().when(repository).deleteByIsbn("1234567890121");

		service.deleteBookByIsbn("1234567890121");

		verify(repository, times(1)).deleteByIsbn("1234567890121");
	}

}