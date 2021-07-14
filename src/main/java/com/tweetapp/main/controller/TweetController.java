package com.tweetapp.main.controller;

import static com.tweetapp.main.util.ControllerConstants.ADD_TWEET;
import static com.tweetapp.main.util.ControllerConstants.*;
import static com.tweetapp.main.util.ControllerConstants.ALL_TWEETS_USERNAME;
import static com.tweetapp.main.util.ControllerConstants.ALL_USERS;
import static com.tweetapp.main.util.ControllerConstants.DELETE_TWEET;
import static com.tweetapp.main.util.ControllerConstants.DISLIKE_TWEET;
import static com.tweetapp.main.util.ControllerConstants.FORGET_PASSWORD;
import static com.tweetapp.main.util.ControllerConstants.LIKE_TWEET;
import static com.tweetapp.main.util.ControllerConstants.LOGIN;
import static com.tweetapp.main.util.ControllerConstants.REGISTER;
import static com.tweetapp.main.util.ControllerConstants.REPLY_TWEET;
import static com.tweetapp.main.util.ControllerConstants.SEARCH_USERNAME;
import static com.tweetapp.main.util.ControllerConstants.TWEETS_API_PATH;
import static com.tweetapp.main.util.ControllerConstants.UPDATE_TWEET;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.main.payload.request.ForgetPasswordRequest;
import com.tweetapp.main.payload.request.RegistrationRequest;
import com.tweetapp.main.payload.request.TweetAddRequest;
import com.tweetapp.main.payload.request.TweetReplyRequest;
import com.tweetapp.main.payload.response.ForgetPasswordResponse;
import com.tweetapp.main.payload.response.LoginResponse;
import com.tweetapp.main.payload.response.RegistrationResponse;
import com.tweetapp.main.payload.response.TweetAddResponse;
import com.tweetapp.main.payload.response.TweetDeleteResponse;
import com.tweetapp.main.payload.response.TweetsResponse;
import com.tweetapp.main.payload.response.UsersResponse;
import com.tweetapp.main.service.TweetService;

@RestController
@RequestMapping(TWEETS_API_PATH)
public class TweetController {

	@Autowired
	TweetService tweetService;

	@GetMapping(LOGIN)
	public ResponseEntity<LoginResponse> login(@NotBlank @RequestParam String username,
			@NotBlank @RequestParam String password) {

		LoginResponse response = tweetService.login(username, password);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(REGISTER)
	public ResponseEntity<RegistrationResponse> registerUser(
			@Valid @RequestBody RegistrationRequest registrationRequest) {

		RegistrationResponse response = tweetService.userRegistration(registrationRequest);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping(FORGET_PASSWORD)
	public ResponseEntity<ForgetPasswordResponse> forgetPassword(@PathVariable String username,
			@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {

		ForgetPasswordResponse response = tweetService.forgetPassword(username, forgetPasswordRequest);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(ALL_TWEETS)
	public ResponseEntity<List<TweetsResponse>> getAllTweets() {

		List<TweetsResponse> response = tweetService.getAllTweets();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(ALL_USERS)
	public ResponseEntity<List<UsersResponse>> getAllUsers() {

		List<UsersResponse> response = tweetService.getAllUsers();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(SEARCH_USERNAME)
	public ResponseEntity<List<UsersResponse>> getUserByUsername(@PathVariable String username) {

		List<UsersResponse> response = tweetService.getUsersByUsername(username);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping(ALL_TWEETS_USERNAME)
	public ResponseEntity<List<TweetsResponse>> getAllTweetsByUsername(@PathVariable String username) {

		List<TweetsResponse> response = tweetService.getAllTweetsByUsername(username);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping(ADD_TWEET)
	public ResponseEntity<TweetAddResponse> addTweet(@PathVariable String username,
			@Valid @RequestBody TweetAddRequest tweetAddRequest) {

		TweetAddResponse response = tweetService.addTweet(username, tweetAddRequest);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(UPDATE_TWEET)
	public ResponseEntity<TweetAddResponse> updateTweet(@PathVariable String username, @PathVariable String id,
			@Valid @RequestBody TweetAddRequest tweetAddRequest) {

		TweetAddResponse response = tweetService.updateTweet(username, id, tweetAddRequest.getTweet());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@DeleteMapping(DELETE_TWEET)
	public ResponseEntity<TweetDeleteResponse> deleteTweet(@PathVariable String username, @PathVariable String id) {

		TweetDeleteResponse response = tweetService.deleteTweet(username, id);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PutMapping(LIKE_TWEET)
	public ResponseEntity<Void> likeTweet(@PathVariable String username, @PathVariable String id) {

		tweetService.likeTweet(username, id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PutMapping(DISLIKE_TWEET)
	public ResponseEntity<Void> dislikeTweet(@PathVariable String username, @PathVariable String id) {

		tweetService.dislikeTweet(username, id);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(REPLY_TWEET)
	public ResponseEntity<Void> replyTweet(@PathVariable String username, @PathVariable String id,
			@Valid @RequestBody TweetReplyRequest replyRequest) {

		tweetService.replyTweet(username, id, replyRequest.getReply());

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping(DELETE_REPLY_TWEET)
	public ResponseEntity<Void> deleteReply(@PathVariable String username, @PathVariable String tweetId,
			@PathVariable String replyId) {

		tweetService.deleteReply(username, replyId, tweetId);

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(TWEET)
	public ResponseEntity<TweetsResponse> getTweet(@PathVariable String tweetId) {

		TweetsResponse response = tweetService.getTweet(tweetId);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}