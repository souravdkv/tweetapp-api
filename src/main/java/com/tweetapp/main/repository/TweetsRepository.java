package com.tweetapp.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.main.entity.Tweets;

@Repository
public interface TweetsRepository extends MongoRepository<Tweets, String> {

	Optional<Tweets> findByUsername(String username);

	List<Tweets> findAllByUsername(String username);
}