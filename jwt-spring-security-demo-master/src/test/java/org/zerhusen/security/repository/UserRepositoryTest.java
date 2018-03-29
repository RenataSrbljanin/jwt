package org.zerhusen.security.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import javax.transaction.Transactional;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.zerhusen.model.security.User;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository repository;
	
	private User user1, user2;
	
	@Before
	public void setUp() throws Exception {
		user1= new User("userOne", "password1", "one@g.com", "fname1", "lname1");	
		user2= new User("userTwo", "password2", "two@g.com", "fname2", "lname2");			
	}
	
	@Test
	public void shouldHaveIdAfterSavingToDb(){
		repository.save(user1);
		long idFromDb = user1.getId();
		assertThat(idFromDb).isNotNull();
	}
	
	@Test
	public void shouldHaveSameCredentialsAfterSavingToDb(){
		User savedUser = repository.save(user1);
		String emailFromDb = savedUser.getEmail();
		String usernameFromDb = savedUser.getUsername();
		assertThat(emailFromDb).isEqualTo(user1.getEmail());
		assertThat(usernameFromDb).isEqualTo(user1.getUsername());
	}
	
	@Test
	public void  shouldVerifyNumberOfUsersInDb(){
		repository.saveAll(Arrays.asList(user1, user2));
		long idCountInRepository = repository.count();
		assertThat(idCountInRepository).isEqualTo(5);
	}
	
	
	
	

}
