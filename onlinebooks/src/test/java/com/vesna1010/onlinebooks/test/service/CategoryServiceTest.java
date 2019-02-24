package com.vesna1010.onlinebooks.test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.CategoryRepository;
import com.vesna1010.onlinebooks.service.impl.CategoryServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

	@Mock
	private CategoryRepository repository;
	@InjectMocks
	private CategoryServiceImpl service;
	private Sort sort;
	private Category category1;
	private Category category2;

	@Before
	public void setUp() {
		sort = new Sort(Direction.ASC, "id");
		category1 = new Category(1L, "Category A");
		category2 = new Category(2L, "Category B");
	}

	@Test
	public void findAllCategoriesTest() {
		when(repository.findAll(sort)).thenReturn(Arrays.asList(category1, category2));

		List<Category> categories = service.findAllCategories(sort);

		assertThat(categories, hasSize(2));
		assertThat(categories.get(0), is(category1));
		assertThat(categories.get(1), is(category2));
		verify(repository, times(1)).findAll(sort);
	}

	@Test
	public void findCategoryByIdTest() {
		when(repository.findById(1L)).thenReturn(Optional.of(category1));

		Category category = service.findCategoryById(1L);

		assertThat(category.getName(), is("Category A"));
		verify(repository, times(1)).findById(1L);
	}

	@Test(expected = RuntimeException.class)
	public void categoryNotFoundTestTest() {
		when(repository.findById(3L)).thenReturn(Optional.empty());

		service.findCategoryById(3L);
	}

	@Test
	public void saveCategoryTest() {
		when(repository.save(category1)).thenReturn(category1);

		Category category = service.saveCategory(category1);

		assertThat(category.getName(), is("Category A"));
		verify(repository, times(1)).save(category1);
	}

	@Test
	public void deleteCategoryByIdTest() {
		doNothing().when(repository).deleteById(1L);

		service.deleteCategoryById(1L);

		verify(repository, times(1)).deleteById(1L);
	}

}