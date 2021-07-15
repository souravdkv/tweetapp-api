package com.tweetapp.main.payload.response;

import java.util.List;

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
public class TweetsResponse {

	private String id;
	
	private String username;

	private String name;

	private String tweets;

	private List<TweetLike> likes;

	private List<TweetReply> replies;
	
	private long postTime;
}