package com.vesna1010.onlinebooks.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.vesna1010.onlinebooks.model.Author;

public interface AuthorService {

	List<Author> findAllAuthors(Sort sort);

	Page<Author> findAuthorsByPage(Pageable pageable);

	Author findAuthorById(Long id);

	Author saveAuthor(Author author);

	void deleteAuthorById(Long id);

}
