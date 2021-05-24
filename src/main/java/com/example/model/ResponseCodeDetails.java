package com.example.model;

public enum ResponseCodeDetails {
	SUCCESS("", "success"),
	FAIL("", "fail"),
	ERROR("", "error"),
	
	UNKNOWN_ERROR("999","Unknown error");

	private final String responseCode;
	private final String responseDescription;

	private ResponseCodeDetails(String responseCode, String responseDescription) {
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
	}

	public String getDescription() {
		return responseDescription;
	}

	public String getCode() {
		return responseCode;
	}

	@Override
	public String toString() {
		return responseCode + ": " + responseDescription;
	}
}