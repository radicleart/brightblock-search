package org.brightblock.search.service.blockstack.models;


import org.brightblock.search.rest.models.IndexedModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ZonefileModel extends IndexedModel {

	private static final long serialVersionUID = -4636203449920344328L;
	private String name;
	private String description;
	private String status;
	private String zonefile;
	@JsonIgnore @JsonProperty("zonefile_txt")
	private String zonefileText;
	@JsonIgnore @JsonProperty("expire_block")
	private String expireBlock;
	private String blockchain;
	@JsonIgnore @JsonProperty("last_txid")
	private String lastTxid;
	private String address;
	@JsonIgnore @JsonProperty("zonefile_hash")
	private String zonefileHash;
	private String profileUrl;
	private String apps;

	public ZonefileModel() {
		super();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getZonefile() {
		return zonefile;
	}

	public void setZonefile(String zonefile) {
		this.zonefile = zonefile;
	}

	@JsonIgnore
	public String getExpireBlock() {
		return (expireBlock != null) ? expireBlock : "";
	}

	public void setExpireBlock(String expireBlock) {
		this.expireBlock = expireBlock;
	}

	@JsonIgnore 
	public String getBlockchain() {
		return blockchain;
	}

	public void setBlockchain(String blockchain) {
		this.blockchain = blockchain;
	}

	@JsonIgnore 
	public String getLastTxid() {
		return lastTxid;
	}

	public void setLastTxid(String lastTxid) {
		this.lastTxid = lastTxid;
	}

	@JsonIgnore 
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@JsonIgnore 
	public String getZonefileHash() {
		return zonefileHash;
	}

	public void setZonefileHash(String zonefileHash) {
		this.zonefileHash = zonefileHash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore 
	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getZonefileText() {
		return zonefileText;
	}

	public void setZonefileText(String zonefileText) {
		this.zonefile = zonefileText;
	}

	public String getApps() {
		return apps;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}

	@Override
	public String toString() {
		return "ZonefileModel [name=" + name + ", status=" + status + ", zonefile=" + zonefile + ", zonefileText="
				+ zonefileText + ", expireBlock=" + expireBlock + ", blockchain=" + blockchain + ", lastTxid="
				+ lastTxid + ", address=" + address + ", zonefileHash=" + zonefileHash + ", profileUrl=" + profileUrl
				+ ", apps=" + apps + "]";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public String[] getDomainGaiaPairs() {
		String apps = this.getApps();
		String[] domainGaiaPairs = apps.split("/\\shttp");
		return domainGaiaPairs;
	}
	
}
