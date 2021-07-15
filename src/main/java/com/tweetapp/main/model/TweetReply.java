package com.tweetapp.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetReply {
	private String id;
	private String name;
	private String username;
	private String message;
	private long replyTime;
}