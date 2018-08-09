package org.brightblock.mam.services.index.posts;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OwnershipRecordsModel implements Serializable {

	private static final long serialVersionUID = 4135803307938030018L;
	private long created;
	private String appUrl;
	private String gaiaUrl;
	private List<OwnershipRecordModel> records;

	@JsonCreator
	public OwnershipRecordsModel(@JsonProperty("created") long created, @JsonProperty("records") List<OwnershipRecordModel> records) {
		super();
		this.created = created;
		this.records = records;
	}

	public long getCreated() {
		return created;
	}

	public void setCreated(long created) {
		this.created = created;
	}

	public List<OwnershipRecordModel> getRecords() {
		return records;
	}

	public void setRecords(List<OwnershipRecordModel> records) {
		this.records = records;
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
