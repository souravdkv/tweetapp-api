package com.tweetapp.main.util;

public class ControllerConstants {
	public static final String TWEETS_API_PATH = "api/v1.0/tweets";
	public static final String REGISTER = "/register";
	public static final String LOGIN = "/login";
	public static final String FORGET_PASSWORD = "/{username}/forget";
	public static final String ALL_TWEETS = "/all";
	public static final String ALL_USERS = "/users/all";
	public static final String SEARCH_USERNAME = "/user/search/{username}";
	public static final String ALL_TWEETS_USERNAME = "/{username}";
	public static final String ADD_TWEET = "/{username}/add";
	public static final String UPDATE_TWEET = "/{username}/update/{id}";
	public static final String DELETE_TWEET = "/{username}/delete/{id}";
	public static final String LIKE_TWEET = "/{username}/like/{id}";
	public static final String DISLIKE_TWEET = "/{username}/dislike/{id}";
	public static final String REPLY_TWEET = "/{username}/reply/{id}";
	public static final String DELETE_REPLY_TWEET = "/{username}/reply/{tweetId}/{replyId}";
	public static final String TWEET = "/tweet/{tweetId}";	
}