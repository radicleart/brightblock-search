package org.brightblock.mam.services.webrtc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.opentok.MediaMode;
import com.opentok.OpenTok;
import com.opentok.Role;
import com.opentok.Session;
import com.opentok.SessionProperties;
import com.opentok.SessionProperties.Builder;
import com.opentok.TokenOptions;
import com.opentok.exception.OpenTokException;

@Service
public class WebrtcServiceImpl implements WebrtcService {

	private static final Logger logger = LogManager.getLogger(WebrtcServiceImpl.class);
	private static int apiKey = 46171452;
	private static String apiSecret = "344d50bfa82e474352cd02157d8279f8533d76d8";
	private static OpenTok opentok;
	static {
		opentok = new OpenTok(apiKey, apiSecret);
	}

	@Override
	@Cacheable(value="tokens", key="{#username, #recordId}")
	public TokenModel getToken(Session session, String username, String recordId) {
		return generateToken(session, username, recordId);
	}

	private TokenModel generateToken(Session session, String username, String recordId) {
		try {
			double expiration = (System.currentTimeMillis() / 1000L) + (7 * 24 * 60 * 60);
			TokenOptions options = new TokenOptions.Builder()
					.role(Role.PUBLISHER)
					.expireTime(expiration)
					.data("username=" + username + ",auctionId=" + recordId)
					.build();
			String token = session.generateToken(options);
			logger.info("New token generated: " + username);
			logger.info("New token generated: " + session.getSessionId());
			logger.info("New token generated: " + token);
			return new TokenModel(session.getSessionId(), token);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	@Cacheable(value="sessions")
	public Session getSession(String recordId) {
		try {
			Builder propertyBuilder = new SessionProperties.Builder();
			// ROUTED: relaying handled by open tok network and so improved bandwidth.
			// RELAYED: relaying handled peer to peer so more decentralised.
			propertyBuilder.mediaMode(MediaMode.RELAYED);
			Session session = opentok.createSession(propertyBuilder.build());
			logger.info("New session generated: " + recordId);
			logger.info("New session generated: " + session.getSessionId());
			return session;
		} catch (OpenTokException e) {
			throw new RuntimeException(e);
		}
	}

}
