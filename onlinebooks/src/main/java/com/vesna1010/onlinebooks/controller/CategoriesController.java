package com.vesna1010.onlinebooks.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vesna1010.onlinebooks.model.Category;
import com.vesna1010.onlinebooks.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired
	private CategoryService service;

	@GetMapping
	public List<Category> findAllCategories(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return service.findAllCategories(sort);
	}

	@GetMapping("/page")
	public Page<Category> findCategoriesByPage(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return service.findCategoriesByPage(pageable);
	}

	@GetMapping("/new")
	public Category category() {
		return new Category();
	}

	@GetMapping("/{id}")
	public Category findCategoryById(@PathVariable Long id) {
		return service.findCategoryById(id);
	}

	@PostMapping
	public String saveCategory(@RequestBody Category category) {
		return "Saved " + service.saveCategory(category);
	}

	@DeleteMapping("/{id}")
	public void deleteCategoryById(@PathVariable Long id) {
		service.deleteCategoryById(id);
	}

}