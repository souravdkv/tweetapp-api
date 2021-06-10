package com.tweetapp.main.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class TweetAppException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6434654148768665881L;

	private final String message;
	private final String errorCode;
}