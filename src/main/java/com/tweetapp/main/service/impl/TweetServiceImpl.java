package com.tweetapp.main.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.main.entity.Tweets;
import com.tweetapp.main.entity.User;
import com.tweetapp.main.exception.model.ErrorCode;
import com.tweetapp.main.model.TweetLike;
import com.tweetapp.main.model.TweetReply;
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
import com.tweetapp.main.repository.TweetsRepository;
import com.tweetapp.main.repository.UserRepository;
import com.tweetapp.main.security.model.UserDetailsImpl;
import com.tweetapp.main.service.TweetService;
import com.tweetapp.main.util.UserUtils;

@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TweetsRepository tweetsRepository;

	@Override
	public RegistrationResponse userRegistration(RegistrationRequest registrationRequest) {
		String username = registrationRequest.getUsername();
		String email = registrationRequest.getEmail();
		String phoneNumber = registrationRequest.getPhoneNumber();

		validateNewUser(username, email, phoneNumber);

		User user = User.builder().username(username).firstName(registrationRequest.getFirstName())
				.lastName(registrationRequest.getLastName()).email(email).phoneNumber(phoneNumber)
				.password(passwordEncoder.encode(registrationRequest.getPassword())).build();

		userRepository.save(user);

		return RegistrationResponse.builder()
				.message(messageSource.getMessage("user.registration.success", null, LocaleContextHolder.getLocale()))
				.build();
	}

	@Override
	public LoginResponse login(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return LoginResponse.builder().name(userDetails.getFirstName() + " " + userDetails.getLastName())
				.username(username).build();
	}

	@Override
	public ForgetPasswordResponse forgetPassword(String username, ForgetPasswordRequest forgetPasswordRequest) {

		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isPresent()
				&& StringUtils.equals(optionalUser.get().getPhoneNumber(), forgetPasswordRequest.getPhoneNumber())) {
			User user = optionalUser.get();

			user.setPassword(passwordEncoder.encode(forgetPasswordRequest.getPassword()));

			userRepository.save(user);

			return ForgetPasswordResponse.builder().message(
					messageSource.getMessage("user.forgetpassword.success", null, LocaleContextHolder.getLocale()))
					.build();
		}

		throw ErrorCode.ERR_INPUT_VALIDATION
				.getException(messageSource.getMessage("user.input.invalid", null, LocaleContextHolder.getLocale()));
	}

	@Override
	public List<TweetsResponse> getAllTweets() {
		List<TweetsResponse> tweetsResponse = new ArrayList<>();
		List<Tweets> tweets = tweetsRepository.findAll();

		Collections.reverse(tweets);

		tweets.forEach(
				tweet -> tweetsResponse.add(TweetsResponse.builder().id(tweet.getId()).username(tweet.getUsername())
						.name(tweet.getFirstName() + " " + tweet.getLastName()).tweets(tweet.getTweet())
						.likes(tweet.getLikes()).replies(tweet.getReplies()).postTime(tweet.getPostTime()).build()));

		return tweetsResponse;
	}

	@Override
	public List<UsersResponse> getAllUsers() {
		List<UsersResponse> allUsers = new ArrayList<>();

		List<User> users = userRepository.findAll();

		users.forEach(user -> allUsers.add(UsersResponse.builder().username(user.getUsername())
				.name(user.getFirstName() + " " + user.getLastName()).build()));

		Comparator<UsersResponse> usersResponseComparator = (t1, t2) -> t1.getName().compareTo(t2.getName());
		allUsers.sort(usersResponseComparator);

		return allUsers;
	}

	@Override
	public List<UsersResponse> getUsersByUsername(String username) {
		List<UsersResponse> usersResponse = new ArrayList<>();

		List<User> users = userRepository.findAllByUsernameStartsWith(username);

		users.forEach(user -> usersResponse.add(UsersResponse.builder().username(user.getUsername())
				.name(user.getFirstName() + " " + user.getLastName()).build()));

		Comparator<UsersResponse> usersResponseComparator = (t1, t2) -> t1.getName().compareTo(t2.getName());
		usersResponse.sort(usersResponseComparator);

		return usersResponse;
	}

	@Override
	public List<TweetsResponse> getAllTweetsByUsername(String username) {
		List<TweetsResponse> tweetsResponse = new ArrayList<>();
		List<Tweets> tweets = tweetsRepository.findAllByUsername(username);

		Collections.reverse(tweets);

		tweets.forEach(
				tweet -> tweetsResponse.add(TweetsResponse.builder().id(tweet.getId()).username(tweet.getUsername())
						.name(tweet.getFirstName() + " " + tweet.getLastName()).tweets(tweet.getTweet())
						.likes(tweet.getLikes()).replies(tweet.getReplies()).postTime(tweet.getPostTime()).build()));

		return tweetsResponse;
	}

	@Override
	public TweetAddResponse addTweet(String username, TweetAddRequest tweetAddRequest) {

		Optional<User> optionalUser = userRepository.findByUsername(username);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			Tweets tweets = Tweets.builder().firstName(user.getFirstName()).lastName(user.getLastName())
					.username(username).tweet(tweetAddRequest.getTweet()).likes(new ArrayList<>())
					.replies(new ArrayList<>()).postTime(Instant.now().getEpochSecond()).build();

			tweetsRepository.save(tweets);

			return TweetAddResponse.builder()
					.message(messageSource.getMessage("user.tweetadd.success", null, LocaleContextHolder.getLocale()))
					.build();
		}

		throw ErrorCode.ERR_INPUT_VALIDATION
				.getException(messageSource.getMessage("user.input.invalid", null, LocaleContextHolder.getLocale()));
	}

	@Override
	public TweetAddResponse updateTweet(String username, String id, String tweet) {

		Optional<Tweets> optionalTweets = tweetsRepository.findById(id);

		if (optionalTweets.isPresent()) {
			Tweets tweets = optionalTweets.get();
			if (StringUtils.equals(tweets.getUsername(), username)) {
				tweets.setTweet(tweet);
				tweets.setPostTime(Instant.now().getEpochSecond());
				tweetsRepository.save(tweets);

				return TweetAddResponse.builder().message(
						messageSource.getMessage("user.tweetupdate.success", null, LocaleContextHolder.getLocale()))
						.build();
			}
		}

		throw ErrorCode.ERR_INPUT_VALIDATION
				.getException(messageSource.getMessage("user.input.invalid", null, LocaleContextHolder.getLocale()));
	}

	@Override
	public TweetDeleteResponse deleteTweet(String username, String id) {

		Optional<Tweets> optionalTweets = tweetsRepository.findById(id);

		if (optionalTweets.isPresent()) {
			Tweets tweets = optionalTweets.get();
			if (StringUtils.equals(tweets.getUsername(), username)) {

				tweetsRepository.delete(tweets);

				return TweetDeleteResponse.builder().message(
						messageSource.getMessage("user.tweetdelete.success", null, LocaleContextHolder.getLocale()))
						.build();
			}
		}

		throw ErrorCode.ERR_INPUT_VALIDATION
				.getException(messageSource.getMessage("user.input.invalid", null, LocaleContextHolder.getLocale()));
	}

	@Override
	public void likeTweet(String username, String id) {
		Optional<Tweets> optionalTweets = tweetsRepository.findById(id);

		if (optionalTweets.isPresent()) {
			Tweets tweets = optionalTweets.get();

			List<TweetLike> likes = tweets.getLikes();

			if (likes.stream().noneMatch(tweet -> StringUtils.equals(username, tweet.getUsername()))) {
				likes.add(TweetLike.builder().username(username).build());

				tweets.setLikes(likes);

				tweetsRepository.save(tweets);
			}
		}
	}

	@Override
	public void dislikeTweet(String username, String id) {
		Optional<Tweets> optionalTweets = tweetsRepository.findById(id);

		if (optionalTweets.isPresent()) {
			Tweets tweets = optionalTweets.get();

			List<TweetLike> likes = tweets.getLikes();

			Predicate<TweetLike> usernamePredicate = tweet -> !StringUtils.equals(username, tweet.getUsername());
			List<TweetLike> updatedLikes = likes.stream().filter(usernamePredicate).collect(Collectors.toList());

			tweets.setLikes(updatedLikes);

			tweetsRepository.save(tweets);
		}
	}

	@Override
	public void replyTweet(String username, String id, String reply) {
		Optional<Tweets> optionalTweets = tweetsRepository.findById(id);
		Optional<User> optionalUser = userRepository.findByUsername(username);
		if (optionalTweets.isPresent() && optionalUser.isPresent()) {
			Tweets tweets = optionalTweets.get();
			User user = optionalUser.get();

			List<TweetReply> replies = tweets.getReplies();
			replies.add(TweetReply.builder().id(UUID.randomUUID().toString()).username(username)
					.name(user.getFirstName() + " " + user.getLastName()).message(reply)
					.replyTime(Instant.now().getEpochSecond()).build());

			tweets.setReplies(replies);
			tweetsRepository.save(tweets);
		}
	}

	private void validateNewUser(String username, String email, String phoneNumber) {

		if (!UserUtils.isValidPhoneNumber(phoneNumber)) {
			throw ErrorCode.ERR_INPUT_VALIDATION.getException(String.format(
					messageSource.getMessage("user.phonenumber.invalid", null, LocaleContextHolder.getLocale()),
					phoneNumber));

		}

		if (userRepository.existsByUsername(username)) {
			throw ErrorCode.ERR_INPUT_VALIDATION.getException(String.format(
					messageSource.getMessage("user.username.inuse", null, LocaleContextHolder.getLocale()), username));

		}

		if (userRepository.existsByEmail(email)) {
			throw ErrorCode.ERR_INPUT_VALIDATION.getException(String.format(
					messageSource.getMessage("user.email.inuse", null, LocaleContextHolder.getLocale()), email));

		}
	}

	@Override
	public void deleteReply(String username, String replyId, String tweetId) {
		Optional<Tweets> optionalTweets = tweetsRepository.findById(tweetId);
		optionalTweets.ifPresent(tweet -> {
			List<TweetReply> replies = tweet.getReplies();
			replies.removeIf(reply -> StringUtils.equals(reply.getId(), replyId)
					&& StringUtils.equals(reply.getUsername(), username));
			tweet.setReplies(replies);
			tweetsRepository.save(tweet);
		});
	}

	@Override
	public TweetsResponse getTweet(String tweetId) {
		Optional<Tweets> optionalTweets = tweetsRepository.findById(tweetId);
		if (optionalTweets.isPresent()) {
			Tweets tweet = optionalTweets.get();
			return TweetsResponse.builder().id(tweet.getId()).name(tweet.getFirstName() + " " + tweet.getLastName())
					.tweets(tweet.getTweet()).likes(tweet.getLikes()).replies(tweet.getReplies())
					.username(tweet.getUsername()).postTime(tweet.getPostTime()).build();
		}

		throw ErrorCode.ERR_INPUT_VALIDATION
				.getException(messageSource.getMessage("user.input.invalid", null, LocaleContextHolder.getLocale()));
	}
}