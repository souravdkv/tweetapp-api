package com.tweetapp.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.main.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
	
	Optional<User> findByPhoneNumber(String phoneNumber);
	
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	List<User> findAllByUsernameStartsWith(String username);
}