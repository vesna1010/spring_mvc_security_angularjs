package com.vesna1010.onlinebooks.test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.jdbc.Sql;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.BookRepository;

@Transactional
@Sql(scripts = "classpath:sql/init.sql")
public class BookRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private BookRepository repository;
	private Pageable pageable;

	{
		Order order1 = new Order(Direction.DESC, "date");
		Order order2 = new Order(Direction.ASC, "isbn");
		Sort sort = Sort.by(order1, order2);

		pageable = PageRequest.of(0, 5, sort);
	}

	@Test
	public void findBooksByPageTest() {
		Page<Book> page1 = repository.findAll(pageable);
		List<Book> books = page1.getContent();

		assertThat(books, hasSize(5));
		assertThat(books.get(0).getTitle(), is("Title (Search) C"));
		assertThat(books.get(1).getTitle(), is("Title (SEARCH) E"));
		assertThat(books.get(2).getTitle(), is("Title (search) A"));
		assertThat(books.get(3).getTitle(), is("Title B"));
		assertThat(books.get(4).getTitle(), is("Title D"));
		assertThat(page1.getTotalPages(), is(1));
	}

	@Test
	public void findBooksByTitleAndPageTest() {
		Page<Book> page1 = repository.findAllByTitleContains("Sear", pageable);
		List<Book> books = page1.getContent();

		assertThat(books, hasSize(3));
		assertThat(books.get(0).getTitle(), is("Title (Search) C"));
		assertThat(books.get(1).getTitle(), is("Title (SEARCH) E"));
		assertThat(books.get(2).getTitle(), is("Title (search) A"));
		assertThat(page1.getTotalPages(), is(1));
	}

	@Test
	public void findBooksByLanguageAndPageTest() {
		Page<Book> page1 = repository.findAllByLanguage(Language.ENGLISH, pageable);
		List<Book> books = page1.getContent();

		assertThat(books, hasSize(3));
		assertThat(books.get(0).getTitle(), is("Title (Search) C"));
		assertThat(books.get(1).getTitle(), is("Title (search) A"));
		assertThat(books.get(2).getTitle(), is("Title D"));
		assertThat(page1.getTotalPages(), is(1));
	}

	@Test
	public void findBooksByCategoryAndPageTest() {
		Page<Book> page1 = repository.findAllByCategoryId(1L, pageable);
		List<Book> books = page1.getContent();

		assertThat(books, hasSize(4));
		assertThat(books.get(0).getTitle(), is("Title (SEARCH) E"));
		assertThat(books.get(1).getTitle(), is("Title (search) A"));
		assertThat(books.get(2).getTitle(), is("Title B"));
		assertThat(books.get(3).getTitle(), is("Title D"));
		assertThat(page1.getTotalPages(), is(1));
	}

	@Test
	public void findBooksByAuthorNameAndPageTest() {
		Page<Book> page1 = repository.findAllDistinctByAuthorsNameContains("Sear", pageable);
		List<Book> books = page1.getContent();

		assertThat(books, hasSize(3));
		assertThat(books.get(0).getTitle(), is("Title (Search) C"));
		assertThat(books.get(1).getTitle(), is("Title (search) A"));
		assertThat(books.get(2).getTitle(), is("Title B"));
		assertThat(page1.getTotalPages(), is(1));
	}

	@Test
	public void findBookByIsbnTest() {
		Optional<Book> optional = repository.findByIsbn("1234567890125");
		Book book = optional.get();

		assertThat(book.getTitle(), is("Title (search) A"));
		assertThat(book.getLanguage(), is(Language.ENGLISH));
		assertThat(book.getCategory().getId(), is(1L));
		assertThat(book.getAuthors(), hasSize(2));
	}

	@Test
	public void bookByIsbnNotFoundTest() {
		Optional<Book> optional = repository.findByIsbn("1234567890126");

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveBookTest() {
		Book book = new Book("1234567890126", "Title E", Language.ENGLISH, new Category(1L, "Category A"),
				getContents(), LocalDate.of(2018, 7, 3));
		book.setAuthors(new HashSet<Author>(Arrays.asList(new Author(1L, "Author C", "authorC@gmail.com", 1955))));

		book = repository.save(book);

		assertThat(book.getTitle(), is("Title E"));
		assertThat(book.getLanguage(), is(Language.ENGLISH));
		assertThat(book.getCategory().getId(), is(1L));
		assertThat(book.getAuthors(), hasSize(1));
		assertThat(repository.count(), is(6L));
	}

	@Test
	public void updateBookTest() {
		Optional<Book> optional = repository.findByIsbn("1234567890125");
		Book book = optional.get();
		book.setTitle("Title");

		book = repository.save(book);
		optional = repository.findByIsbn("1234567890125");
		book = optional.get();

		assertThat(book.getTitle(), is("Title"));
		assertThat(book.getLanguage(), is(Language.ENGLISH));
		assertThat(book.getCategory().getId(), is(1L));
		assertThat(book.getAuthors(), hasSize(2));
		assertThat(repository.count(), is(5L));
	}

	@Test
	public void deleteBookByIsbnTest() {
		repository.deleteByIsbn("1234567890125");

		Optional<Book> optional = repository.findByIsbn("1234567890125");

		assertFalse(optional.isPresent());
	}

}