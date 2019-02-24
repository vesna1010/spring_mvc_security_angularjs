package com.vesna1010.onlinebooks.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.Author;
import com.vesna1010.onlinebooks.model.Book;
import com.vesna1010.onlinebooks.repository.AuthorRepository;
import com.vesna1010.onlinebooks.repository.BookRepository;
import com.vesna1010.onlinebooks.service.AuthorService;

@Transactional
@Service("authorService")
public class AuthorServiceImpl implements AuthorService {

	@Resource
	private AuthorRepository authorRepository;
	@Resource
	private BookRepository bookRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Author> findAllAuthors(Sort sort) {
		Iterable<Author> authors = authorRepository.findAll(sort);

		return (List<Author>) authors;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Author> findAuthorsByPage(Pageable pageable) {
		return (Page<Author>) authorRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Author findAuthorById(Long id) {
		Optional<Author> optional = authorRepository.findById(id);

		return (Author) optional.orElseThrow(() -> new ResourceNotFoundException("No author found with id " + id));
	}

	@Override
	public Author saveAuthor(Author author) {
		return (Author) authorRepository.save(author);
	}

	@Override
	public void deleteAuthorById(Long id) {
		Author author = findAuthorById(id);
		Set<Book> books = author.getBooks();

		for (Book book : books) {
			Set<Author> authors = book.getAuthors();

			if (authors.size() == 1) {
				bookRepository.deleteByIsbn(book.getIsbn());
			} else {
				book.getAuthors().remove(author);
				bookRepository.save(book);
			}
		}

		authorRepository.deleteById(author.getId());
	}

}
