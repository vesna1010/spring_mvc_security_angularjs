package com.vesna1010.onlinebooks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.service.BookService;

@RestController
@RequestMapping("/books")
public class BooksController {

	@Autowired
	private BookService service;

	@GetMapping
	public Page<Book> findBooks(@PageableDefault(page = 0, size = 10) @SortDefaults({
			@SortDefault(sort = "date", direction = Direction.DESC),
			@SortDefault(sort = "isbn", direction = Direction.ASC) }) Pageable pageable) {
		return service.findBooksByPage(pageable);
	}

	@GetMapping("/title/{title}")
	public Page<Book> findBooksByTitleAndPage(@PathVariable String title,
			@PageableDefault(page = 0, size = 10) @SortDefaults({
					@SortDefault(sort = "date", direction = Direction.DESC),
					@SortDefault(sort = "isbn", direction = Direction.ASC) }) Pageable pageable) {
		return service.findBooksByTitleAndPage(title, pageable);
	}

	@GetMapping("/author/{name}")
	public Page<Book> findBooksByAuthorNameAndPage(@PathVariable String name,
			@PageableDefault(page = 0, size = 10) @SortDefaults({
					@SortDefault(sort = "date", direction = Direction.DESC),
					@SortDefault(sort = "isbn", direction = Direction.ASC) }) Pageable pageable) {
		return service.findBooksByAuthorNameAndPage(name, pageable);
	}

	@GetMapping("/category/{id}")
	public Page<Book> findBooksByCategoryIdAndPage(@PathVariable Long id,
			@PageableDefault(page = 0, size = 10) @SortDefaults({
					@SortDefault(sort = "date", direction = Direction.DESC),
					@SortDefault(sort = "isbn", direction = Direction.ASC) }) Pageable pageable) {
		return service.findBooksByCategoryIdAndPage(id, pageable);
	}

	@GetMapping("/language/{language}")
	public Page<Book> findBooksByLanguageAndPage(@PathVariable Language language,
			@PageableDefault(page = 0, size = 10) @SortDefaults({
					@SortDefault(sort = "date", direction = Direction.DESC),
					@SortDefault(sort = "isbn", direction = Direction.ASC) }) Pageable pageable) {
		return service.findBooksByLanguageAndPage(language, pageable);
	}

	@GetMapping("/page")
	public Page<Book> findBooksByPage(
			@PageableDefault(page = 0, size = 10, sort = "isbn", direction = Direction.ASC) Pageable pageable) {
		return service.findBooksByPage(pageable);
	}

	@GetMapping("/new")
	public Book book() {
		return new Book();
	}

	@GetMapping("/{isbn}")
	public Book findBookByIsbn(@PathVariable String isbn) {
		return service.findBookByIsbn(isbn);
	}

	@PostMapping
	public String saveBook(@RequestBody Book book) {
		return "Saved " + service.saveBook(book);
	}

	@GetMapping(value = "/download/{isbn}")
	public byte[] downloadBookByIsbn(@PathVariable String isbn) {
		Book book = service.findBookByIsbn(isbn);

		return book.getContents();
	}

	@DeleteMapping("/{isbn}")
	public void deleteBookByIsbn(@PathVariable String isbn) {
		service.deleteBookByIsbn(isbn);
	}

}