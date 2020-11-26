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
@TypeAlias(value = "RatesModel")
public class RatesModel {

	private List<RateModel> ratesModel;

	public RatesModel() {
		super();
	}
	public static class Deserializer extends StdDeserializer<RatesModel> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public RatesModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			RatesModel im = new RatesModel();
			return im;
		}
	}
}
