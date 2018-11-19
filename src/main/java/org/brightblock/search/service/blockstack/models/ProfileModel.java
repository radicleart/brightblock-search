package org.brightblock.search.service.blockstack.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileModel implements Serializable {

	private static final long serialVersionUID = 7783052417583473920L;
	private String publicKey;
	private String issuerPublicKey;
	private String subjectPublicKey;
	private String apps;
	private List<String> websites;
	private List<String> otherImages = new ArrayList<String>();
	private String contentImage;
	private String avatarImage;
	private String name;
	private String description;
	private String signature;
	private String expiresAt;
	private String issuedAt;
	private String alg;
	private String typ;
	private List<Map<String, Object>> account;
	private Map<String, String> address;
	
	@SuppressWarnings("unchecked")
	@JsonCreator
	public ProfileModel(@JsonProperty("token") String token,
			@JsonProperty("decodedToken") Map<String, Object> decodedToken) {
		super();
		parseHeader(decodedToken);
		
		Map<String, Object> payload = (Map<String, Object>) decodedToken.get("payload");
		this.signature = (String) decodedToken.get("signature");
		parseTimestamps(payload);
		parseKeys(payload);

		Map<String, Object> claim = (Map<String, Object>) payload.get("claim");
		parseImages(claim);
		parseWebsites(claim);
		parseApps(claim);
		this.account = (List<Map<String, Object>>) claim.get("account");
		this.address = (Map<String, String>) claim.get("address");
		this.name = (String) claim.get("name");
		parseDescription(claim);
	}
	
	@JsonIgnore
	private void parseDescription(Map<String, Object> claim) {
		this.description = (String) claim.get("description");
		if (this.description == null) {
			this.description = "";
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	private void parseApps(Map<String, Object> claim) {
		if (claim.containsKey("apps")) {
			StringBuilder sb = new StringBuilder();
			Map<String, String> apps = (Map<String, String>) claim.get("apps");
			for (String app : apps.keySet()) {
				sb.append(app).append("=").append(apps.get(app)).append(" ");
			}
			this.apps = sb.toString();
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	private void parseKeys(Map<String, Object> payload) {
		if (payload.containsKey("issuer")) {
			Map<String, Object> issuer = (Map<String, Object>) payload.get("issuer");
			this.issuerPublicKey = (String) issuer.get("publicKey");
		}
		if (payload.containsKey("subject")) {
			Map<String, Object> subject = (Map<String, Object>) payload.get("subject");
			this.subjectPublicKey = (String) subject.get("publicKey");
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	private void parseHeader(Map<String, Object> decodedToken) {
		Map<String, Object> header = (Map<String, Object>) decodedToken.get("header");
		this.alg = (String)header.get("alg");
		this.typ = (String)header.get("typ");
	}

	@JsonIgnore
	private void parseTimestamps(Map<String, Object> payload) {
		if (payload.containsKey("expiresAt")) {
			this.expiresAt = (String) payload.get("expiresAt");
		} else if (payload.containsKey("exp")) {
			this.expiresAt = (String) payload.get("exp");
		}
		if (payload.containsKey("issuedAt")) {
			this.issuedAt = (String) payload.get("issuedAt");
		} else if (payload.containsKey("iat")) {
			this.issuedAt = (String) payload.get("iat");
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	private void parseWebsites(Map<String, Object> claim) {
		if (claim.containsKey("website")) {
			List<Map<String, String>> websites = (List<Map<String, String>>) claim.get("website");
			for (Map<String, String> site : websites) {
				this.websites.add((String) site.get("url"));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	private void parseImages(Map<String, Object> claim) {
		if (claim.containsKey("image")) {
			List<Map<String, String>> images = (List<Map<String, String>>) claim.get("image");
			for (Map<String, String> thisImage : images) {
				if (thisImage.containsValue("cover")) {
					this.contentImage = thisImage.get("contentUrl");
				} else if (thisImage.containsValue("avatar")) {
					this.avatarImage = thisImage.get("contentUrl");
				} else {
					this.otherImages.add(thisImage.get("contentUrl"));
				}
			}
		}
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getIssuerPublicKey() {
		return issuerPublicKey;
	}

	public void setIssuerPublicKey(String issuerPublicKey) {
		this.issuerPublicKey = issuerPublicKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getApps() {
		return apps;
	}

	public void setApps(String apps) {
		this.apps = apps;
	}

	public List<String> getWebsites() {
		return websites;
	}

	public void setWebsites(List<String> websites) {
		this.websites = websites;
	}

	public String getContentImage() {
		return contentImage;
	}

	public void setContentImage(String contentImage) {
		this.contentImage = contentImage;
	}

	public String getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(String avatarImage) {
		this.avatarImage = avatarImage;
	}

	public String getSubjectPublicKey() {
		return subjectPublicKey;
	}

	public void setSubjectPublicKey(String subjectPublicKey) {
		this.subjectPublicKey = subjectPublicKey;
	}

	public List<String> getOtherImages() {
		return otherImages;
	}

	public void setOtherImages(List<String> otherImages) {
		this.otherImages = otherImages;
	}

	public String getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	public String getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(String issuedAt) {
		this.issuedAt = issuedAt;
	}

	public String getAlg() {
		return alg;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public List<Map<String, Object>> getAccount() {
		return account;
	}

	public void setAccount(List<Map<String, Object>> account) {
		this.account = account;
	}

	public Map<String, String> getAddress() {
		return address;
	}

	public void setAddress(Map<String, String> address) {
		this.address = address;
	}

}
