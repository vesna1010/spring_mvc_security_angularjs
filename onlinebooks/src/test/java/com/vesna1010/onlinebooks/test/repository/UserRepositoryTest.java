package com.vesna1010.onlinebooks.test.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import com.vesna1010.onlinebooks.enums.Authority;
import com.vesna1010.onlinebooks.model.User;
import com.vesna1010.onlinebooks.repository.UserRepository;

@Transactional
@Sql(scripts = "classpath:sql/init.sql")
public class UserRepositoryTest extends BaseRepositoryTest {

	@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder encoder;
	private Pageable pageable;

	{
		pageable = PageRequest.of(0, 2, new Sort(Direction.ASC, "username"));
	}

	@Test
	public void findUsersByPageTestTest() {
		Page<User> page1 = repository.findAll(pageable);
		List<User> users = page1.getContent();

		assertThat(users.get(0).getUsername(), is("UsernameA"));
		assertThat(users.get(1).getUsername(), is("UsernameB"));
		assertThat(page1.getTotalPages(), is(2));
	}

	@Test
	public void findUserByUsernameTest() {
		Optional<User> optional = repository.findByUsername("UsernameA");
		User user = optional.get();

		assertThat(user.getEmail(), is("emailA@gmail.com"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
	}

	@Test
	public void userByUsernameNotFoundTest() {
		Optional<User> optional = repository.findByUsername("UsernameE");

		assertFalse(optional.isPresent());
	}

	@Test
	public void saveUserTest() {
		String password = encoder.encode("Password");
		User user = repository.save(new User("Username", password, "email@gmail.com", Authority.USER));

		assertThat(user.getUsername(), is("Username"));
		assertThat(user.getPassword(), is(password));
		assertTrue(user.isEnabled());
		assertThat(repository.count(), is(5L));
	}

	@Test
	public void updateUserTest() {
		String password = encoder.encode("Password");
		Optional<User> optional = repository.findByUsername("UsernameA");
		User user = optional.get();

		user.setPassword(password);
		user = repository.save(user);

		optional = repository.findByUsername("UsernameA");
		user = optional.get();

		assertThat(user.getUsername(), is("UsernameA"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertThat(user.getPassword(), is(password));
		assertThat(repository.count(), is(4L));
	}

	@Test
	public void deleteUserByUsernameTest() {
		repository.deleteByUsername("UsernameD");

		Optional<User> optional = repository.findByUsername("UsernameD");

		assertFalse(optional.isPresent());
	}

	@Test
	public void disableUserByUsernameTest() {
		repository.disableByUsername("UsernameD");

		Optional<User> optional = repository.findByUsername("UsernameD");
		User user = optional.get();

		assertFalse(user.isEnabled());
	}

}
