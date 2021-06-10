package com.tweetapp.main.exception.model;

import com.tweetapp.main.exception.TweetAppException;

public enum ErrorCode {
	ERR_TECHNICAL, 
	ERR_NOT_FOUND, 
	ERR_ACCESS_DENIED, 
	ERR_INPUT_VALIDATION, 
	ERR_UPGRADE_REQUIRED,
	ERR_ACCOUNT_DEACTIVATED,
	ERR_HEADER_MISMATCH;

	private String getCode() {
		return name();
	}

	public TweetAppException getException(String message) {
		return new TweetAppException(message, getCode());
	}
}