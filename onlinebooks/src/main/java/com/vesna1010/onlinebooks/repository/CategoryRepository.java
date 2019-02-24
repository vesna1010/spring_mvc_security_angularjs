package com.vesna1010.onlinebooks.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vesna1010.onlinebooks.model.Category;

@SuppressWarnings("unchecked")
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findAll(Sort sort);
	
	Page<Category> findAll(Pageable pageable);

	Optional<Category> findById(Long id);

	Category save(Category category);

	void deleteById(Long id);

}