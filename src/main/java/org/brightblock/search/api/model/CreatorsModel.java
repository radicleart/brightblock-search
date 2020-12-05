package org.brightblock.search.api.model;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.Id;
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
@TypeAlias(value = "CreatorsModel")
public class CreatorsModel {

	@Id private String uuid;
	private Set<String> creators;

	public CreatorsModel() {
		super();
		this.uuid = UUID.randomUUID().toString();
	}
	public static class Deserializer extends StdDeserializer<CreatorsModel> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public CreatorsModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			CreatorsModel im = new CreatorsModel();
			return im;
		}
	}
}
