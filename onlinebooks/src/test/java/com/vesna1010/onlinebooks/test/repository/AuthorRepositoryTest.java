package com.vesna1010.onlinebooks.test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.jdbc.Sql;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.repository.AuthorRepository;

@Sql(scripts = "classpath:sql/init.sql")
public class AuthorRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private AuthorRepository repository;
	private Sort sort;
	private Pageable pageable;

	{
		Order order1 = new Order(Direction.ASC, "name");
		Order order2 = new Order(Direction.ASC, "id");
		sort = Sort.by(order1, order2);
		pageable = PageRequest.of(0, 2, sort);
	}

	@Test
	public void findAllAuthorsTest() {
		List<Author> authors = repository.findAll(sort);

		assertThat(authors, hasSize(4));
		assertThat(authors.get(0).getEmail(), is("authorA2@gmail.com"));
		assertThat(authors.get(1).getEmail(), is("authorC@gmail.com"));
		assertThat(authors.get(2).getEmail(), is("authorA1@gmail.com"));
		assertThat(authors.get(3).getEmail(), is("authorB@gmail.com"));
	}

	@Test
	public void findAuthorsByPageTest() {
		Page<Author> page1 = repository.findAll(pageable);
		List<Author> authors = page1.getContent();

		assertThat(authors, hasSize(2));
		assertThat(authors.get(0).getEmail(), is("authorA2@gmail.com"));
		assertThat(authors.get(1).getEmail(), is("authorC@gmail.com"));
		assertThat(page1.getTotalPages(), is(2));
	}

	@Test
	public void findAuthorByIdTest() {
		Optional<Author> optional = repository.findById(1L);
		Author author = optional.get();

		assertThat(author.getName(), is("Author C"));
		assertThat(author.getEmail(), is("authorC@gmail.com"));
		assertThat(author.getYearOfBirth(), is(1955));
		assertThat(author.getBooks(), hasSize(1));
	}

	@Test
	public void authorByIdNotFoundTest() {
		Optional<Author> optional = repository.findById(5L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveAuthorTest() {
		Author author = new Author("Author", "author@gmail.com", 1965);

		author = repository.save(author);

		assertNotNull(author.getId());
		assertThat(repository.count(), is(5L));
	}

	@Test
	public void updateAuthorTest() {
		Optional<Author> optional = repository.findById(1L);
		Author author = optional.get();

		author.setName("Author");
		author = repository.save(author);

		optional = repository.findById(1L);
		author = optional.get();

		assertThat(author.getName(), is("Author"));
		assertThat(author.getBooks(), hasSize(1));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void deleteAuthorByIdTest() {
		repository.deleteById(1L);

		Optional<Author> optional = repository.findById(1L);

		assertFalse(optional.isPresent());
	}

}