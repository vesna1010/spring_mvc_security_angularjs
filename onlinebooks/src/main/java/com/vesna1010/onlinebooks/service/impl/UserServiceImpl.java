package com.vesna1010.onlinebooks.service.impl;

import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.User;
import com.vesna1010.onlinebooks.repository.UserRepository;
import com.vesna1010.onlinebooks.service.UserService;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserRepository repository;
	@Resource
	private PasswordEncoder encoder;

	@Override
	@Transactional(readOnly = true)
	public Page<User> findUsersByPage(Pageable pageable) {
		return (Page<User>) repository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserByUsername(String username) {
		Optional<User> optional = repository.findByUsername(username);

		return (User) optional
				.orElseThrow(() -> new ResourceNotFoundException("No user found with username " + username));
	}

	@Override
	public User saveUser(User user) {
		setEncodedPassword(user);

		return (User) repository.save(user);
	}

	private void setEncodedPassword(User user) {
		String password = user.getPassword();

		password = encoder.encode(password);

		user.setPassword(password);
	}

	@Override
	public void disableUserByUsername(String username) {
		repository.disableByUsername(username);
	}

	@Override
	public void deleteUserByUsername(String username) {
		repository.deleteByUsername(username);
	}

}