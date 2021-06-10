package com.tweetapp.main.service;

import com.tweetapp.main.payload.request.RegistrationRequest;
import com.tweetapp.main.payload.response.LoginResponse;
import com.tweetapp.main.payload.response.RegistrationResponse;

public interface TweetService {

	RegistrationResponse userRegistration(RegistrationRequest registrationRequest);

	LoginResponse login(String username, String password);

}