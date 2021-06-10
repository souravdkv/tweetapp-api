package com.tweetapp.main.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.tweetapp.main.entity.User;
import com.tweetapp.main.repository.UserRepository;
import com.tweetapp.main.security.model.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserDetails userDetails = null;
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			userDetails = UserDetailsImpl.build(user.get());
		}
		return userDetails;
	}
}
