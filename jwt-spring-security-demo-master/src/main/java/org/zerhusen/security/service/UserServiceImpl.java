package org.zerhusen.security.service;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerhusen.customexceptions.UsernameAlreadyExistsException;
import org.zerhusen.model.security.Authority;
import org.zerhusen.model.security.User;
import org.zerhusen.security.repository.AuthorityRepository;
import org.zerhusen.security.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository,
			BCryptPasswordEncoder passwordEncoder,
			AuthorityRepository authorityRepository) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authorityRepository = authorityRepository;
		
	}

	public User save(User user) {

		if(userWithThatUsernameAlreadyExists(user))
			throw new UsernameAlreadyExistsException();
		
		User newUser = new User();
		
		newUser.setUsername(user.getUsername());
		newUser.setEmail(user.getEmail());
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setAuthorities(new ArrayList<Authority> (authorityRepository.findAll()));
		newUser.setEnabled(true);
		newUser.setLastPasswordResetDate(new Date());
		
		return userRepository.save(newUser);
		
	}

	private boolean userWithThatUsernameAlreadyExists(User user) {
		
		return userRepository.findByUsername(user.getUsername()) != null;
	}

}
