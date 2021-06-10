package com.tweetapp.main.controller;

import static com.tweetapp.main.util.ControllerConstants.LOGIN;
import static com.tweetapp.main.util.ControllerConstants.REGISTER;
import static com.tweetapp.main.util.ControllerConstants.TWEETS_API_PATH;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.main.payload.request.RegistrationRequest;
import com.tweetapp.main.payload.response.LoginResponse;
import com.tweetapp.main.payload.response.RegistrationResponse;
import com.tweetapp.main.service.TweetService;

@RestController
@RequestMapping(TWEETS_API_PATH)
public class TweetController {

    @Autowired
    TweetService tweetService;

    @GetMapping(LOGIN)
    public ResponseEntity<LoginResponse> login(@NotBlank @RequestParam String username, @NotBlank @RequestParam String password){
        LoginResponse response = tweetService.login(username, password);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(REGISTER)
    public ResponseEntity<RegistrationResponse> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest){
    	RegistrationResponse response = tweetService.userRegistration(registrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}