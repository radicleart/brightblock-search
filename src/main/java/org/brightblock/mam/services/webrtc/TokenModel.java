package org.brightblock.mam.services.webrtc;

import java.io.Serializable;

public class TokenModel implements Serializable {

	public TokenModel() {
		super();
	}

	public TokenModel(String sessionId, String token) {
		super();
		this.sessionId = sessionId;
		this.token = token;
	}

	private static final long serialVersionUID = -3414640374223584587L;
	private String sessionId;
	private String token;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
