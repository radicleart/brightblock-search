package org.brightblock.mam.services.webrtc;

import com.opentok.Session;

public interface WebrtcService
{
	public TokenModel getToken(Session session, String username, String recordId);
	public Session getSession(String recordId);
}
