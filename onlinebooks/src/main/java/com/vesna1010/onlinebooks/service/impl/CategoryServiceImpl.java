package com.vesna1010.onlinebooks.service.impl;

import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.repository.CategoryRepository;
import com.vesna1010.onlinebooks.service.CategoryService;

@Transactional
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

	@Resource
	private CategoryRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Category> findAllCategories(Sort sort) {
		return (List<Category>) repository.findAll(sort);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Category> findCategoriesByPage(Pageable pageable) {
		return (Page<Category>) repository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Category findCategoryById(Long id) {
		Optional<Category> optional = repository.findById(id);

		return (Category) optional.orElseThrow(() -> new ResourceNotFoundException("No category found with id " + id));
	}

	@Override
	public Category saveCategory(Category category) {
		return (Category) repository.save(category);
	}

	@Override
	public void deleteCategoryById(Long id) {
		repository.deleteById(id);
	}

}