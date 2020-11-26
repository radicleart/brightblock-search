package org.brightblock.search.api.model;

import java.io.IOException;

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
@TypeAlias(value = "DomainIndexFileModel")
public class DomainIndexFileModel {

	private String indexFileName;
	private String category;

	public DomainIndexFileModel() {
		super();
	}

	public static class Deserializer extends StdDeserializer<DomainIndexFileModel> {

		private static final long serialVersionUID = -6362610850500342012L;

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public DomainIndexFileModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			DomainIndexFileModel im = new DomainIndexFileModel();
			im.setIndexFileName(node.get("indexFileName").asText());
			im.setCategory(node.get("category").asText());
			return im;
		}
	}

}
