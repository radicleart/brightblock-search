package org.brightblock.search.rest.models;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ApiModel implements IApiModel {

	private static final long serialVersionUID = 4405282857342855499L;
	private boolean failed;
	private long timestamp;
	private String httpStatus;
	private Object details;
	private String message;
	@JsonIgnore
	private String name;
	@JsonIgnore
	private ForwardHeaderModel headersModel;
	
	
	public ApiModel() {
		super();
		this.timestamp = (new Date()).getTime();
	}

	@JsonIgnore
	public void setHeaders(HttpServletRequest request) {
		headersModel = new ForwardHeaderModel();
		headersModel.setHeaderForwardedProto(request.getHeader(ForwardHeaderModel.X_FORWARDED_PROTO));
		headersModel.setHeaderForwardedFor(request.getHeader(ForwardHeaderModel.X_FORWARDED_FOR));
		headersModel.setHeaderForwardedHost(request.getHeader(ForwardHeaderModel.X_FORWARDED_HOST));
		headersModel.setHeaderForwardedServer(request.getHeader(ForwardHeaderModel.X_FORWARDED_SERVER));
		headersModel.setHeaderRealIp(request.getHeader(ForwardHeaderModel.X_REAL_IP));
	}
	
	@JsonIgnore
	public static ApiModel getFailure(ResponseCodes apiResponse, String details) {
		ApiModel model = new ApiModel();
		model.setFailed(true);
		model.setDetails(details);
		model.setMessage(apiResponse.getDescription());
		model.setHttpStatus(apiResponse.name());
		return model;
	}
	
//	@JsonIgnore
//	public static ApiModel getSuccess(ResponseCodes apiResponse, String details) {
//		ApiModel model = new ApiModel();
//		model.setDetails(details);
//		model.setMessage(apiResponse.getDescription());
//		model.setHttpStatus(apiResponse.name());
//		return model;
//	}
	
	@JsonIgnore
	public static ApiModel getSuccess(ResponseCodes apiResponse, Object details) {
		ApiModel model = new ApiModel();
		model.setDetails(details);
		model.setMessage(apiResponse.getDescription());
		model.setHttpStatus(apiResponse.name());
		return model;
	}
	
	@JsonIgnore
	public static ApiModel getSuccess(ResponseCodes apiResponse, IApiModel details) {
		ApiModel model = new ApiModel();
		model.setDetails(details);
		model.setMessage(apiResponse.getDescription());
		model.setHttpStatus(apiResponse.name());
		return model;
	}
	
	@JsonIgnore
	public static ApiModel getSuccess(ResponseCodes apiResponse, ApiModel details) {
		ApiModel model = new ApiModel();
		model.setDetails(details);
		model.setMessage(apiResponse.getDescription());
		model.setHttpStatus(apiResponse.name());
		return model;
	}
	
	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	public ForwardHeaderModel getHeadersModel() {
		return headersModel;
	}

	public void setHeadersModel(ForwardHeaderModel headersModel) {
		this.headersModel = headersModel;
	}
}
