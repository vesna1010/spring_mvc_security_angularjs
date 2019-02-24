package com.vesna1010.onlinebooks.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.onlinebooks.model.Category;

public interface CategoryService {

	List<Category> findAllCategories(Sort sort);
	
	Page<Category> findCategoriesByPage(Pageable pageable);

	Category findCategoryById(Long id);

	Category saveCategory(Category category);

	void deleteCategoryById(Long id);

}