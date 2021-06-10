package com.tweetapp.main.exception.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorInfo {
	private HttpStatus status;
	private String errorCode;
	private List<String> errors;
	private Date timestamp;

	public ErrorInfo(HttpStatus status, String errorCode, String error, Date timestamp) {
		this.errorCode = errorCode;
		this.timestamp = timestamp;
		this.status = status;
		this.errors = Arrays.asList(error);
	}

	public ErrorInfo(HttpStatus status, String errorCode, List<String> errors, Date timestamp) {
		this.errorCode = errorCode;
		this.timestamp = timestamp;
		this.status = status;
		this.errors = errors;
	}
}