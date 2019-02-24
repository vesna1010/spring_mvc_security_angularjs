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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.jdbc.Sql;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.CategoryRepository;

@Sql(scripts = "classpath:sql/init.sql")
public class CategoryRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private CategoryRepository repository;
	private Sort sort = new Sort(Direction.ASC, "id");

	@Test
	public void findAllCategoriesTest() {
		List<Category> categories = repository.findAll(sort);

		assertThat(categories, hasSize(3));
		assertThat(categories.get(0).getName(), is("Category A"));
		assertThat(categories.get(1).getName(), is("Category B"));
		assertThat(categories.get(2).getName(), is("Category C"));
	}

	@Test
	public void findCategoryByIdTest() {
		Optional<Category> optional = repository.findById(1L);
		Category category = optional.get();

		assertThat(category.getName(), is("Category A"));
		assertThat(category.getBooks(), hasSize(4));
	}

	@Test
	public void categoryByIdNotFoundTest() {
		Optional<Category> optional = repository.findById(4L);

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveCategoryTest() {
		Category category = new Category("Category");

		category = repository.save(category);

		assertNotNull(category.getId());
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void updateCategoryTest() {
		Optional<Category> optional = repository.findById(1L);
		Category category = optional.get();

		category.setName("Category");
		category = repository.save(category);

		optional = repository.findById(1L);
		category = optional.get();

		assertThat(category.getName(), is("Category"));
		assertThat(category.getBooks(), hasSize(4));
		assertThat(repository.count(), is(3L));
	}

	@Test
	public void deleteCategoryByIdTest() {
		repository.deleteById(1L);

		Optional<Category> optional = repository.findById(1L);

		assertFalse(optional.isPresent());
	}

}