package com.tweetapp.main.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.main.model.TweetLike;
import com.tweetapp.main.model.TweetReply;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tweets")
public class Tweets {
	@Id
	private String id;

	private String username;

	private String tweet;

	private String firstName;

	private String lastName;

	private List<TweetLike> likes;

	private List<TweetReply> replies;
	
	private long postTime;
}