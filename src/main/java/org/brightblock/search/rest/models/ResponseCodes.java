package org.brightblock.search.rest.models;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;



@JsonSerialize(using = ResponseCodesTypeEnumSerializer.class)
public enum ResponseCodes {
	OK(HttpStatus.OK.value(), "Success!"),
	NOT_FOUND(HttpStatus.NOT_FOUND.value(), "The resource could not be found on our servers. Please try another request."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "Oops! An unexpected error prevented completion of your request. The problem has been reported and will be addressed as soon as possible. Thanks for your patience."),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE.value(), "The resource is unavailable because an upstream service is not responding.");

	private int status;
	private String description;

	private ResponseCodes(int status, String description) {
		this.status = status;
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	};
}
