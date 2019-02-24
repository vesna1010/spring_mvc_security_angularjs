package com.vesna1010.onlinebooks.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.vesna1010.onlinebooks.model.User;

@SuppressWarnings("unchecked")
public interface UserRepository extends JpaRepository<User, String> {

	Page<User> findAll(Pageable pageable);

	Optional<User> findByUsername(String username);

	User save(User user);

	@Modifying
	@Query("update User u set u.enabled=false where u.username=:username")
	void disableByUsername(@Param("username") String username);

	void deleteByUsername(String username);

}