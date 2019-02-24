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
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.service.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

	@Autowired
	private AuthorService service;

	@GetMapping
	public List<Author> findAllAuthors(@SortDefaults({ @SortDefault(sort = "name", direction = Direction.ASC),
			@SortDefault(sort = "id", direction = Direction.ASC) }) Sort sort) {
		return service.findAllAuthors(sort);
	}

	@GetMapping("/page")
	public Page<Author> findAuthorsByPage(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
		return service.findAuthorsByPage(pageable);
	}

	@GetMapping("/new")
	public Author author() {
		return new Author();
	}

	@GetMapping("/{id}")
	public Author findAuthorById(@PathVariable Long id) {
		return service.findAuthorById(id);
	}

	@PostMapping
	public String saveAuthor(@RequestBody Author author) {
		return "Saved " + service.saveAuthor(author);
	}

	@DeleteMapping("/{id}")
	public void deleteAuthorById(@PathVariable Long id) {
		service.deleteAuthorById(id);
	}

}