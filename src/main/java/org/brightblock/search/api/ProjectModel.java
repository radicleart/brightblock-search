package org.brightblock.search.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = ProjectModel.Deserializer.class)
public class ProjectModel extends IndexableModel {

	private static final long serialVersionUID = -1711100172168977732L;
	private String projectId;
	private Long created;

	public ProjectModel() {
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

	public static class Deserializer extends StdDeserializer<ProjectModel> {
		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public ProjectModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			ProjectModel im = new ProjectModel();
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
			
			if (node.has("projectId")) {
				im.setProjectId(node.get("projectId").asText());
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
			if (node.has("objType")) {
				im.setObjType(node.get("objType").asText());
			}
			if (node.has("domain")) {
				im.setDomain(node.get("domain").asText());
			}
			if (node.has("imageUrl")) {
				im.setAssetUrl(node.get("imageUrl").asText());
			} else if (node.has("assetUrl")) {
				im.setAssetUrl(node.get("assetUrl").asText());
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
}
