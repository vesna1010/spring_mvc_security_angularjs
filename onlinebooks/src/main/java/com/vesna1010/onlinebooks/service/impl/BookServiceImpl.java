package com.vesna1010.onlinebooks.service.impl;

import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.repository.BookRepository;
import com.vesna1010.onlinebooks.service.BookService;

@Transactional
@Service("bookService")
public class BookServiceImpl implements BookService {

	@Resource
	private BookRepository repository;

	@Override
	@Transactional(readOnly = true)
	public Page<Book> findBooksByPage(Pageable pageable) {
		return (Page<Book>) repository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Book> findBooksByTitleAndPage(String title, Pageable pageable) {
		return (Page<Book>) repository.findAllByTitleContains(title, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Book> findBooksByAuthorNameAndPage(String authorName, Pageable pageable) {
		return (Page<Book>) repository.findAllDistinctByAuthorsNameContains(authorName, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Book> findBooksByCategoryIdAndPage(Long id, Pageable pageable) {
		return (Page<Book>) repository.findAllByCategoryId(id, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Book> findBooksByLanguageAndPage(Language language, Pageable pageable) {
		return (Page<Book>) repository.findAllByLanguage(language, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Book findBookByIsbn(String isbn) {
		Optional<Book> optional = repository.findByIsbn(isbn);

		return (Book) optional.orElseThrow(() -> new ResourceNotFoundException("No book found with isbn " + isbn));
	}

	@Override
	public Book saveBook(Book book) {
		return (Book) repository.save(book);
	}

	@Override
	public void deleteBookByIsbn(String isbn) {
		repository.deleteByIsbn(isbn);
	}

}