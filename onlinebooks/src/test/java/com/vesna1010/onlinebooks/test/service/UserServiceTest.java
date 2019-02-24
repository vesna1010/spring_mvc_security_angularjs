package com.vesna1010.onlinebooks.test.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vesna1010.onlinebooks.enums.Authority;
import com.vesna1010.onlinebooks.exception.ResourceNotFoundException;
import com.vesna1010.onlinebooks.model.User;
import com.vesna1010.onlinebooks.repository.UserRepository;
import com.vesna1010.onlinebooks.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository repository;
	@Mock
	private PasswordEncoder encoder;
	@InjectMocks
	private UserServiceImpl service;
	private Pageable pageable;
	private User user1;
	private User user2;

	@Before
	public void setUp() {
		Sort sort = new Sort(Direction.ASC, "username");
		pageable = PageRequest.of(0, 10, sort);

		user1 = new User("UsernameA", "PasswordA", "emailA@gmail.com", Authority.ADMIN);
		user2 = new User("UsernameB", "PasswordB", "emailB@gmail.com", Authority.USER);
	}

	@Test
	public void findUsersByPageTest() {
		when(repository.findAll(pageable)).thenReturn(new PageImpl<User>(Arrays.asList(user1, user2)));

		Page<User> page = service.findUsersByPage(pageable);
		List<User> users = page.getContent();

		assertThat(page.getTotalPages(), is(1));
		assertThat(users, hasSize(2));
		assertThat(users.get(0), is(user1));
		assertThat(users.get(1), is(user2));
		verify(repository, times(1)).findAll(pageable);
	}

	@Test
	public void findUserByUsernameTest() {
		when(repository.findByUsername("UsernameA")).thenReturn(Optional.of(user1));

		User user = service.findUserByUsername("UsernameA");

		assertThat(user.getEmail(), is("emailA@gmail.com"));
		assertThat(user.getAuthority(), is(Authority.ADMIN));
		assertTrue(user.isEnabled());
		verify(repository, times(1)).findByUsername("UsernameA");
	}

	@Test(expected = ResourceNotFoundException.class)
	public void userNotFoundTest() {
		when(repository.findByUsername("UsernameC")).thenReturn(Optional.empty());

		service.findUserByUsername("UsernameC");
	}

	@Test
	public void saveUserTest() {
		when(repository.save(user1)).thenReturn(user1);

		User user = service.saveUser(user1);

		assertThat(user.getUsername(), is("UsernameA"));
		assertThat(user.getEmail(), is("emailA@gmail.com"));
		verify(repository, times(1)).save(user1);
		verify(encoder, times(1)).encode("PasswordA");
	}

	@Test
	public void deleteUserByUsernameTest() {
		doNothing().when(repository).deleteByUsername("UsernameA");

		service.deleteUserByUsername("UsernameA");

		verify(repository, times(1)).deleteByUsername("UsernameA");
	}

	@Test
	public void disableUserByUsernameTest() {
		doNothing().when(repository).disableByUsername("UsernameA");

		service.disableUserByUsername("UsernameA");

		verify(repository, times(1)).disableByUsername("UsernameA");
	}

}