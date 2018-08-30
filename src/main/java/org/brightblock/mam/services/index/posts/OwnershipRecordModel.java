package org.brightblock.mam.services.index.posts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OwnershipRecordModel implements Serializable, Comparable<OwnershipRecordModel> {

	private static final long serialVersionUID = 8617555047178804394L;
	private String id;
	private String title;
	private String description;
	private String uploader;
	private String itemType;
	private String keywords;
	private Boolean registered;
	private String appUrl;
	private String gaiaUrl;
	private SaleDataModel saleData;

	public OwnershipRecordModel() {
		super();
	}

	@JsonCreator
	public OwnershipRecordModel(@JsonProperty("id") String id, 
			@JsonProperty("uploader") String uploader, 
			@JsonProperty("title") String title, 
			@JsonProperty("description") String description, 
			@JsonProperty("itemType") String itemType, 
			@JsonProperty("keywords") String keywords, 
			@JsonProperty("registered") Boolean registered) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.uploader = uploader;
		this.itemType = itemType;
		this.keywords = keywords;
		if (registered != null) {
			this.registered = registered;
		} else {
			 this.registered = false;
		}
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUploader() {
		return uploader;
	}

	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	public Boolean getRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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
	public int compareTo(OwnershipRecordModel model) {
		return this.id.compareTo(model.getId());
	}

	@Override
	public boolean equals(Object model) {
		OwnershipRecordModel record = (OwnershipRecordModel) model;
		return this.id.equals(record.getId());
	}

	public SaleDataModel getSaleData() {
		return saleData;
	}

	public void setSaleData(SaleDataModel saleData) {
		this.saleData = saleData;
	}
}
