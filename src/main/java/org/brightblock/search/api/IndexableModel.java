package org.brightblock.search.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = IndexableModel.Deserializer.class)
public class IndexableModel implements SearchResultModel, Serializable, Comparable<IndexableModel> {

	private static final long serialVersionUID = 7471051478505916999L;
	private String id;
	private String projectId;
	private String title;
	private String description;
	private Long created;
	private Long updated;
	private Long mintedOn;
	private String owner;
	private String assetHash;
	private String assetUrl;
	private String assetProjectUrl;
	private String privacy;
	private String artist;
	private String gallerist;
	private String galleryId;
	private String objType;
	private String domain;
	private KeywordModel category;
	private List<KeywordModel> keywords;
	private String status;
	private String buyer;
	private String txid;
	private Map<String, String> metaData;

	public IndexableModel() {
		super();
		created = new Date().getTime();
		updated = new Date().getTime();
	}

	// @JsonCreator
	// public IndexableModel(@JsonAlias({"id", "auctionId"}) String id,
	// @JsonProperty("title") String title,
	// @JsonProperty("description") String description,
	// @JsonProperty("objType") String objType,
	// @JsonProperty("owner") String owner,
	// @JsonProperty("domain") String domain,
	// @JsonProperty("keywords") String keywords) {
	// super();
	// this.id = id;
	// this.title = title;
	// this.description = description;
	// this.owner = owner;
	// this.keywords = keywords;
	// this.objType = objType;
	// this.domain = domain;
	// }

	public static class Deserializer extends StdDeserializer<IndexableModel> {
		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public IndexableModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			IndexableModel im = new IndexableModel();
			ObjectMapper mapper = new ObjectMapper();
			if (node.has("assetHash")) {
				im.setId(node.get("assetHash").asText());
			} else if (node.has("auctionId")) {
				im.setId(node.get("auctionId").asText());
			} else if (node.has("galleryId")) {
				im.setId(node.get("galleryId").asText());
			} else if (node.has("privacy")) {
				im.setId(node.get("privacy").asText());
			} else if (node.has("gallerist")) {
				im.setId(node.get("gallerist").asText());
			} else if (node.has("id")) {
				im.setId(node.get("id").asText());
			} else {
				throw new RuntimeException("unable to parse");
			}
			
			if (node.has("assetHash")) {
				im.setAssetHash(node.get("assetHash").asText());
			}
			
			if (node.has("projectId")) {
				im.setProjectId(node.get("projectId").asText());
			}
			
			if (node.has("imageUrl")) {
				im.setAssetUrl(node.get("imageUrl").asText());
			} else if (node.has("assetUrl")) {
				im.setAssetUrl(node.get("assetUrl").asText());
			}
			
			if (node.has("assetProjectUrl")) {
				im.setAssetProjectUrl(node.get("assetProjectUrl").asText());
			} else if (node.has("externalUrl")) {
				im.setAssetProjectUrl(node.get("externalUrl").asText());
			}
			
			if (node.has("title")) {
				im.setTitle(node.get("title").asText());
			} else {
				im.setTitle(node.get("name").asText());
			}
			if (node.has("owner")) {
				im.setOwner(node.get("owner").asText());
			} else if (node.has("auctioneer")) {
				im.setOwner(node.get("auctioneer").asText());
			} else if (node.has("administrator")) {
				im.setOwner(node.get("administrator").asText());
			} else {
				throw new RuntimeException("unable to parse");
			}
			if (node.has("description")) {
				im.setDescription(node.get("description").asText());
			} else {
				im.setDescription(node.get("title").asText());
			}
			if (node.has("mintedOn")) {
				im.setArtist(node.get("mintedOn").asText());
			}
			if (node.has("artist")) {
				im.setArtist(node.get("artist").asText());
			}
			if (node.has("objType")) {
				im.setObjType(node.get("objType").asText());
			}
			if (node.has("domain")) {
				im.setDomain(node.get("domain").asText());
			}
			if (node.has("buyer")) {
				im.setBuyer(node.get("buyer").asText());
			}
			if (node.has("status")) {
				im.setStatus(node.get("status").asText());
			}
			if (node.has("txid")) {
				im.setTxid(node.get("txid").asText());
			}
			if (node.has("category")) {
				mapper = new ObjectMapper();
				@SuppressWarnings("unchecked") LinkedHashMap<String, Object> cat = mapper.convertValue(node.get("category"), LinkedHashMap.class);
				Object lev = cat.get("level");
				Integer level = (Integer)lev;
				KeywordModel km = new KeywordModel((String)cat.get("id"), (String)cat.get("name"), level, (String)cat.get("parent"));
				im.setCategory(km);
			}
			if (node.has("keywords")) {
				mapper = new ObjectMapper();
				try {
					@SuppressWarnings("unchecked") List<LinkedHashMap<String, Object>> keywords = mapper.convertValue(node.get("keywords"), List.class);
					List<KeywordModel> kms = new ArrayList<>();
					KeywordModel km = null;
					for (LinkedHashMap<String, Object> keyword : keywords) {
						Object lev = keyword.get("level");
						Integer level = (Integer)lev;
						km = new KeywordModel((String)keyword.get("id"), (String)keyword.get("name"), level, (String)keyword.get("parent"));
						kms.add(km);
					}
					im.setKeywords(kms);
				} catch (Exception e) {
					@SuppressWarnings("unchecked") List<String> keywords = mapper.convertValue(node.get("keywords"), List.class);
					List<KeywordModel> kms = new ArrayList<>();
					KeywordModel km = null;
					for (String keyword : keywords) {
						km = new KeywordModel("1", keyword, 1, null);
						kms.add(km);
					}
					im.setKeywords(kms);
				}
			}
			if (node.has("metaData")) {
				mapper = new ObjectMapper();
				@SuppressWarnings("unchecked") Map<String, String> result = mapper.convertValue(node.get("metaData"), Map.class);
				im.setMetaData(result);
			}
			return im;
		}
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getAssetProjectUrl() {
		return assetProjectUrl;
	}

	public void setAssetProjectUrl(String assetProjectUrl) {
		this.assetProjectUrl = assetProjectUrl;
	}

	public String getAssetUrl() {
		return assetUrl;
	}

	public void setAssetUrl(String assetUrl) {
		this.assetUrl = assetUrl;
	}

	public String getAssetHash() {
		return assetHash;
	}

	public void setAssetHash(String assetHash) {
		this.assetHash = assetHash;
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public List<KeywordModel> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<KeywordModel> keywords) {
		this.keywords = keywords;
	}

	@Override
	public int compareTo(IndexableModel model) {
		return this.id.compareTo(model.getId());
	}

	@Override
	public boolean equals(Object model) {
		IndexableModel record = (IndexableModel) model;
		return this.id.equals(record.getId());
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	public String getGallerist() {
		return gallerist;
	}

	public void setGallerist(String gallerist) {
		this.gallerist = gallerist;
	}

	public String getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(String galleryId) {
		this.galleryId = galleryId;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getUpdated() {
		return updated;
	}

	public void setUpdated(Long updated) {
		this.updated = updated;
	}

	public KeywordModel getCategory() {
		return category;
	}

	public void setCategory(KeywordModel category) {
		this.category = category;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public Long getMintedOn() {
		return mintedOn;
	}

	public void setMintedOn(Long mintedOn) {
		this.mintedOn = mintedOn;
	}

}
