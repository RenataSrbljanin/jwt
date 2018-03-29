package org.zerhusen.security.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zerhusen.customexceptions.UsernameAlreadyExistsException;
import org.zerhusen.model.security.User;
import org.zerhusen.security.repository.AuthorityRepository;
import org.zerhusen.security.repository.UserRepository;

public class UserServiceTest {

	@Autowired
	private BCryptPasswordEncoder passwordEncoderMock = mock(BCryptPasswordEncoder.class);
	
	@Autowired
	private UserRepository userRepositoryMock = mock(UserRepository.class);
	
	@Autowired
	private AuthorityRepository authorityRepositoryMock = mock(AuthorityRepository.class);
	
	private final String USERNAME = "user";
	private final String PASSWORD ="password";
	private final String EMAIL = "email@g.com";
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	private UserServiceImpl usi = new UserServiceImpl(
			userRepositoryMock, passwordEncoderMock, authorityRepositoryMock);
	
	@Test
	public void shouldReturnUserWhenSavinUserToDb(){
		User userToBeSaved = new User(USERNAME, PASSWORD, EMAIL);
		when(userRepositoryMock.save(any(User.class))).thenReturn(userToBeSaved);
		User savedUser = usi.save(userToBeSaved);
		assertThat(savedUser).isNotNull();
	}
	
	@Test
	public void shouldThrowExceptionWhenUsernameAlreadyExistsInDb(){
		User userToBeSaved = new User(USERNAME, PASSWORD, EMAIL);
		when(userRepositoryMock.findByUsername(userToBeSaved.getUsername())).thenReturn(userToBeSaved);
		exception.expect(UsernameAlreadyExistsException.class);
		usi.save(userToBeSaved);
	}
}
