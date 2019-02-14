package org.brightblock.search.api;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = IndexableModel.Deserializer.class)
public class IndexableModel implements Serializable, Comparable<IndexableModel> {

	private static final long serialVersionUID = 7471051478505916999L;
	private String id;
	private String title;
	private String description;
	private String owner;
	private String artist;
	private String objType;
	private String domain;
	private String keywords;

	public IndexableModel() {
		super();
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
			if (node.has("id")) {
				im.setId(node.get("id").asText());
			} else if (node.has("auctionId")) {
				im.setId(node.get("auctionId").asText());
			} else {
				throw new RuntimeException("unable to parse");
			}
			im.setTitle(node.get("title").asText());
			im.setDescription(node.get("description").asText());
			if (node.has("owner")) {
				im.setOwner(node.get("owner").asText());
			} else if (node.has("administrator")) {
				im.setOwner(node.get("administrator").asText());
			} else {
				throw new RuntimeException("unable to parse");
			}
			if (node.has("artist")) {
				im.setArtist(node.get("artist").asText());
			}
			if (node.has("keywords")) {
				im.setKeywords(node.get("keywords").asText());
			}
			if (node.has("objType")) {
				im.setObjType(node.get("objType").asText());
			}
			if (node.has("domain")) {
				im.setDomain(node.get("domain").asText());
			}
			return im;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
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

}
