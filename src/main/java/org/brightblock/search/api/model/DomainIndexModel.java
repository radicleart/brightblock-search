package org.brightblock.search.api.model;

import java.io.IOException;
import java.util.List;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
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
@TypeAlias(value = "DomainIndexModel")
public class DomainIndexModel {

	private String projectId;
	private String owner;
	private String domain;
	private String storeageModel;
	private List<DomainIndexFileModel> indexFiles;

	public DomainIndexModel() {
		super();
	}
	
	public static class Deserializer extends StdDeserializer<DomainIndexModel> {
		private static final long serialVersionUID = -3545556837562223751L;

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public DomainIndexModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			DomainIndexModel im = new DomainIndexModel();
			im.setProjectId(node.get("projectId").asText());
			im.setOwner(node.get("owner").asText());
			im.setDomain(node.get("domain").asText());
			im.setStoreageModel(node.get("storeageModel").asText());
			return im;
		}
	}
}
