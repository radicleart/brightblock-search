package org.brightblock.mam.rest.models;

public class BlogModel implements IApiModel {

	private static final long serialVersionUID = 366486045137887476L;
	private ResponseCodes[] responseCodes = ResponseCodes.values();
	private String jsonRepresentation;

	public String getJsonRepresentation() {
		return jsonRepresentation;
	}

	public void setJsonRepresentation(String jsonRepresentation) {
		this.jsonRepresentation = jsonRepresentation;
	}

	public ResponseCodes[] getResponseCodes() {
		return responseCodes;
	}

	public void setResponseCodes(ResponseCodes[] responseCodes) {
		this.responseCodes = responseCodes;
	}

}
