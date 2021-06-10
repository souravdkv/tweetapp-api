package com.tweetapp.main.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.main.entity.User;
import com.tweetapp.main.exception.model.ErrorCode;
import com.tweetapp.main.payload.request.RegistrationRequest;
import com.tweetapp.main.payload.response.LoginResponse;
import com.tweetapp.main.payload.response.RegistrationResponse;
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

	private void validateNewUser(String username, String email, String phoneNumber) {

		if (!UserUtils.isValidPhoneNumber(phoneNumber)) {
			throw ErrorCode.ERR_INPUT_VALIDATION.getException(String.format(
					messageSource.getMessage("user.phonenumber.invalid", null, LocaleContextHolder.getLocale()),
					username));

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
}