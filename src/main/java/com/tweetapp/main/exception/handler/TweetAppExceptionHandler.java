package com.tweetapp.main.exception.handler;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.tweetapp.main.exception.TweetAppException;
import com.tweetapp.main.exception.model.ErrorCode;
import com.tweetapp.main.exception.model.ErrorInfo;
import com.tweetapp.main.kafka.producer.TweetAppKafkaProducer;

@ControllerAdvice
public class TweetAppExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LogManager.getLogger(TweetAppExceptionHandler.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private TweetAppKafkaProducer kafkaProducer;

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
		String message = messageSource.getMessage("user.login.failed", null, LocaleContextHolder.getLocale());
		String loggingMessage = "BadCredentialsException with Details: " + message;
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		return new ResponseEntity<>(
				new ErrorInfo(HttpStatus.UNAUTHORIZED, ErrorCode.ERR_ACCESS_DENIED.toString(), message, new Date()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(InternalAuthenticationServiceException.class)
	public ResponseEntity<Object> handleInternalAuthenticationServiceException(
			InternalAuthenticationServiceException ex) {
		String message = messageSource.getMessage("user.login.failed", null, LocaleContextHolder.getLocale());

		String loggingMessage = "InternalAuthenticationServiceException with Details: " + message;
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		return new ResponseEntity<>(
				new ErrorInfo(HttpStatus.UNAUTHORIZED, ErrorCode.ERR_ACCESS_DENIED.toString(), message, new Date()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
		String loggingMessage = ex.getMessage();
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		String message = messageSource.getMessage("user.access.denied", null, LocaleContextHolder.getLocale());
		return new ResponseEntity<>(
				new ErrorInfo(HttpStatus.UNAUTHORIZED, ErrorCode.ERR_ACCESS_DENIED.toString(), message, new Date()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(TweetAppException.class)
	public ResponseEntity<Object> handleTweetAppException(TweetAppException ex) {
		String loggingMessage = "TweetAppException with Details: " + ex.getMessage();
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		if (ex.getErrorCode().equals(ErrorCode.ERR_NOT_FOUND.toString())) {
			return new ResponseEntity<>(
					new ErrorInfo(HttpStatus.NOT_FOUND, ex.getErrorCode(), ex.getMessage(), new Date()),
					HttpStatus.NOT_FOUND);
		} else if (ex.getErrorCode().equals(ErrorCode.ERR_INPUT_VALIDATION.toString())) {
			return new ResponseEntity<>(
					new ErrorInfo(HttpStatus.BAD_REQUEST, ex.getErrorCode(), ex.getMessage(), new Date()),
					HttpStatus.BAD_REQUEST);
		} else if (ex.getErrorCode().equals(ErrorCode.ERR_UPGRADE_REQUIRED.toString())) {
			return new ResponseEntity<>(
					new ErrorInfo(HttpStatus.UPGRADE_REQUIRED, ex.getErrorCode(), ex.getMessage(), new Date()),
					HttpStatus.UPGRADE_REQUIRED);
		} else if (ex.getErrorCode().equals(ErrorCode.ERR_ACCOUNT_DEACTIVATED.toString())) {
			return new ResponseEntity<>(
					new ErrorInfo(HttpStatus.NOT_ACCEPTABLE, ex.getErrorCode(), ex.getMessage(), new Date()),
					HttpStatus.NOT_ACCEPTABLE);
		} else if (ex.getErrorCode().equals(ErrorCode.ERR_ACCESS_DENIED.toString())) {
			return new ResponseEntity<>(
					new ErrorInfo(HttpStatus.UNAUTHORIZED, ex.getErrorCode(), ex.getMessage(), new Date()),
					HttpStatus.UNAUTHORIZED);
		} else if (ex.getErrorCode().equals(ErrorCode.ERR_HEADER_MISMATCH.toString())) {
			return new ResponseEntity<>(new ErrorInfo(HttpStatus.BAD_REQUEST, ErrorCode.ERR_HEADER_MISMATCH.toString(),
					ex.getMessage(), new Date()), HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR,
					ErrorCode.ERR_TECHNICAL.toString(), ex.getMessage(), new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String loggingMessage = "HttpMessageNotReadableException with Details: " + ex.getMessage();
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		JsonMappingException jsonMappingException = (JsonMappingException) ex.getCause();
		List<String> errors = jsonMappingException.getPath().stream()
				.map(jme -> String.format(
						messageSource.getMessage("request.field.format.invalid", null, LocaleContextHolder.getLocale()),
						jme.getFieldName()))
				.collect(Collectors.toList());
		return buildResponseEntity(
				new ErrorInfo(HttpStatus.BAD_REQUEST, ErrorCode.ERR_INPUT_VALIDATION.toString(), errors, new Date()));
	}

	private ResponseEntity<Object> buildResponseEntity(ErrorInfo errorInfo) {
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(x -> String.format(
						messageSource.getMessage("request.argument.invalid", null, LocaleContextHolder.getLocale()),
						x.getField(), x.getDefaultMessage()))
				.collect(Collectors.toList());

		String loggingMessage = "MethodArgumentNotValidException with Details: " + errors;
		kafkaProducer.publish(loggingMessage);
		LOG.error(loggingMessage);

		return new ResponseEntity<>(
				new ErrorInfo(HttpStatus.BAD_REQUEST, ErrorCode.ERR_INPUT_VALIDATION.toString(), errors, new Date()),
				HttpStatus.BAD_REQUEST);
	}
}