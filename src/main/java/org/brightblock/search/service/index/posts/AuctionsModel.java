package org.brightblock.search.service.index.posts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuctionsModel implements Serializable {

	private static final long serialVersionUID = -8892194966672872913L;
	private long created;
	private String appUrl;
	private String gaiaUrl;
	private List<AuctionModel> auctions;

	@JsonCreator
	public AuctionsModel(@JsonProperty("created") long created, @JsonProperty("auctions") List<AuctionModel> auctions) {
		super();
		this.created = created;
		this.auctions = auctions;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public List<AuctionModel> getAuctions() {
		return auctions;
	}

	public void setRecords(List<AuctionModel> auctions) {
		this.auctions = auctions;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getGaiaUrl() {
		return gaiaUrl;
	}

	public void setGaiaUrl(String gaiaUrl) {
		this.gaiaUrl = gaiaUrl;
	}

}
