package org.brightblock.mam.services.blockstack.models;


import org.brightblock.mam.rest.models.IndexedModel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZonefileModel extends IndexedModel {

	private static final long serialVersionUID = -4636203449920344328L;
//status": "registered", ""
	// personal.id space? zonefile_txt": "$ORIGIN 0.id\n$TTL 3600\n_http._tcp URI 10 1 \"https://gaia.blockstack.org/hub/126adtBDqoyuPR56dEnJkuK4xntRysyfXs/0/profile.json\"\n", 
	//zonefile": "$ORIGIN 0.id\n$TTL 3600\n_http._tcp URI 10 1 \"https://gaia.blockstack.org/hub/126adtBDqoyuPR56dEnJkuK4xntRysyfXs/0/profile.json\"\n", 
//expire_block": 598115, "
//blockchain": "bitcoin", "
//last_txid": "6e17d692e6ab0563682b951db0b3377047014a1d00ea770f59500c97f710c674", "
//address": "126adtBDqoyuPR56dEnJkuK4xntRysyfXs", "
//zonefile_hash": "fabda66de86f9fe5feada86857bbeb89650fba21"
	private String name;
	private String description;
	private String status;
	private String zonefile;
	@JsonProperty("zonefile_txt")
	private String zonefileText;
	@JsonProperty("expire_block")
	private String expireBlock;
	private String blockchain;
	@JsonProperty("last_txid")
	private String lastTxid;
	private String address;
	@JsonProperty("zonefile_hash")
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

	public String getExpireBlock() {
		return (expireBlock != null) ? expireBlock : "";
	}

	public void setExpireBlock(String expireBlock) {
		this.expireBlock = expireBlock;
	}

	public String getBlockchain() {
		return blockchain;
	}

	public void setBlockchain(String blockchain) {
		this.blockchain = blockchain;
	}

	public String getLastTxid() {
		return lastTxid;
	}

	public void setLastTxid(String lastTxid) {
		this.lastTxid = lastTxid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

}
