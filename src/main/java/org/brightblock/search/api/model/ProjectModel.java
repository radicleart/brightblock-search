package org.brightblock.search.api.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@JsonDeserialize(using = ProjectModel.Deserializer.class)
public class ProjectModel {

	private String projectId;
	private Long created;
	private String id;
	private String title;
	private String description;
	private Long updated;
	private Long mintedOn;
	private String owner;
	private String objType;
	private String domain;
	private String imageUrl;

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
			if (node.has("projectId")) {
				im.setId(node.get("projectId").asText());
			} else {
				throw new RuntimeException("unable to parse");
			}
			if (node.has("title")) {
				im.setTitle(node.get("title").asText());
			}
			if (node.has("owner")) {
				im.setOwner(node.get("owner").asText());
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
				im.setImageUrl(node.get("imageUrl").asText());
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
