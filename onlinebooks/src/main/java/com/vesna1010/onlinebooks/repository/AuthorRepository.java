package com.vesna1010.onlinebooks.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.onlinebooks.model.Author;

@SuppressWarnings("unchecked")
public interface AuthorRepository extends JpaRepository<Author, Long> {

	List<Author> findAll(Sort sort);

	Page<Author> findAll(Pageable pageable);

	Optional<Author> findById(Long id);

	Author save(Author author);

	void deleteById(Long id);

}