package com.vesna1010.onlinebooks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.model.Book;

public interface BookService {

	Page<Book> findBooksByPage(Pageable pageable);

	Page<Book> findBooksByTitleAndPage(String title, Pageable pageable);

	Page<Book> findBooksByAuthorNameAndPage(String authorName, Pageable pageable);

	Page<Book> findBooksByCategoryIdAndPage(Long id, Pageable pageable);

	Page<Book> findBooksByLanguageAndPage(Language language, Pageable pageable);

	Book findBookByIsbn(String isbn);

	Book saveBook(Book book);

	void deleteBookByIsbn(String isbn);

}