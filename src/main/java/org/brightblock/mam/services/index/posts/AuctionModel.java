package org.brightblock.mam.services.index.posts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuctionModel implements Serializable, Comparable<AuctionModel> {

	private static final long serialVersionUID = 196475387724169407L;
	private Long auctionId;
	private String title;
	private String description;
	private String privacy;
	private String auctioneer;
	private String administrator;
	private String auctionType;
	private String keywords;
	private Long startDate;
	private Long endDate;
	private String appUrl;
	private String gaiaUrl;

	public AuctionModel() {
		super();
	}

	@JsonCreator
	public AuctionModel(@JsonProperty("auctionId") String auctionId, 
			@JsonProperty("privacy") String privacy, 
			@JsonProperty("title") String title, 
			@JsonProperty("description") String description, 
			@JsonProperty("auctionType") String auctionType, 
			@JsonProperty("auctioneer") String auctioneer, 
			@JsonProperty("administrator") String administrator, 
			@JsonProperty("startDate") Long startDate, 
			@JsonProperty("endDate") Long endDate, 
			@JsonProperty("keywords") String keywords) {
		super();
		this.auctionId = Long.valueOf(auctionId);
		this.title = title;
		this.description = description;
		this.privacy = privacy;
		this.auctioneer = auctioneer;
		this.administrator = administrator;
		this.startDate = startDate;
		this.endDate = endDate;
		this.auctionType = auctionType;
		this.keywords = keywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	@Override
	public int compareTo(AuctionModel model) {
		return this.auctionId.compareTo(model.getAuctionId());
	}

	@Override
	public boolean equals(Object model) {
		AuctionModel record = (AuctionModel) model;
		return this.auctionId.equals(record.getAuctionId());
	}

	public Long getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(Long auctionId) {
		this.auctionId = auctionId;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getAuctioneer() {
		return auctioneer;
	}

	public void setAuctioneer(String auctioneer) {
		this.auctioneer = auctioneer;
	}

	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}

	public String getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(String auctionType) {
		this.auctionType = auctionType;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

}
