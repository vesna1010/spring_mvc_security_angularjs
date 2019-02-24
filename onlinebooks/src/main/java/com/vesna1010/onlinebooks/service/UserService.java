package com.vesna1010.onlinebooks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.vesna1010.onlinebooks.model.User;

public interface UserService {

	Page<User> findUsersByPage(Pageable pageable);

	User findUserByUsername(String username);

	User saveUser(User user);

	void disableUserByUsername(String username);

	void deleteUserByUsername(String username);

}