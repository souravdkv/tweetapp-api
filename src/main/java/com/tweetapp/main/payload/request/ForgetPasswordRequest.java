package com.tweetapp.main.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordRequest {
	@NotBlank
	private String phoneNumber;

	@NotBlank
	@Size(min = 8)
	private String password;
}