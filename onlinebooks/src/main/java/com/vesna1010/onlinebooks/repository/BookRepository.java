package com.vesna1010.onlinebooks.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.onlinebooks.enums.Language;
import com.vesna1010.onlinebooks.model.Book;

@SuppressWarnings("unchecked")
public interface BookRepository extends JpaRepository<Book, String> {

	Page<Book> findAll(Pageable pageable);

	Page<Book> findAllByTitleContains(String value, Pageable pageable);

	Page<Book> findAllByLanguage(Language language, Pageable pageable);

	Page<Book> findAllByCategoryId(Long id, Pageable pageable);

	Page<Book> findAllDistinctByAuthorsNameContains(String value, Pageable pageable);

	Optional<Book> findByIsbn(String isbn);

	Book save(Book book);

	void deleteByIsbn(String isbn);

}