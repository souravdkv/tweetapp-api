package com.tweetapp.main.util;

import java.util.regex.Pattern;

public class UserUtils {
	private UserUtils() {

	}

	private static final String PHONE_NUMBER_REGEX = "^[6-9]\\d{9}$";

	public static boolean isValidPhoneNumber(String phoneNumber) {
		return validateDataWithRegex(PHONE_NUMBER_REGEX, phoneNumber);
	}

	private static boolean validateDataWithRegex(String regex, String data) {
		Pattern pat = Pattern.compile(regex);
		if (data == null)
			return false;
		return pat.matcher(data).matches();
	}

}
