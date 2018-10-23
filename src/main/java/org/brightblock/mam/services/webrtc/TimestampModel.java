package org.brightblock.mam.services.webrtc;

import java.io.Serializable;
import java.util.Date;

/**
 * Synchronise time on client with server time.
 * @author mikey
 *
 */
public class TimestampModel implements Serializable {

	private static final long serialVersionUID = 2202765284502977212L;
	private Long timestamp;

	public TimestampModel() {
		super();
		this.timestamp = new Date().getTime();
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
