package com.tweetapp.main.service;

import java.util.List;

import com.tweetapp.main.payload.request.ForgetPasswordRequest;
import com.tweetapp.main.payload.request.RegistrationRequest;
import com.tweetapp.main.payload.request.TweetAddRequest;
import com.tweetapp.main.payload.response.ForgetPasswordResponse;
import com.tweetapp.main.payload.response.LoginResponse;
import com.tweetapp.main.payload.response.RegistrationResponse;
import com.tweetapp.main.payload.response.TweetAddResponse;
import com.tweetapp.main.payload.response.TweetDeleteResponse;
import com.tweetapp.main.payload.response.TweetsResponse;
import com.tweetapp.main.payload.response.UsersResponse;

public interface TweetService {

	RegistrationResponse userRegistration(RegistrationRequest registrationRequest);

	LoginResponse login(String username, String password);

	ForgetPasswordResponse forgetPassword(String username, ForgetPasswordRequest forgetPasswordRequest);

	List<TweetsResponse> getAllTweets();

	List<UsersResponse> getAllUsers();

	List<UsersResponse> getUsersByUsername(String username);

	List<TweetsResponse> getAllTweetsByUsername(String username);

	TweetAddResponse addTweet(String username, TweetAddRequest tweetAddRequest);

	TweetAddResponse updateTweet(String username, String id, String tweet);

	TweetDeleteResponse deleteTweet(String username, String id);

	void likeTweet(String username, String id);

	void dislikeTweet(String username, String id);

	void replyTweet(String username, String id, String reply);

	void deleteReply(String username, String replyId, String tweetId);

	TweetsResponse getTweet(String tweetId);
}