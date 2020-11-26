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
@TypeAlias(value = "RateModel")
public class RateModel {

	private String symbol;
	private String fiatCurrency;
	private Double amountFiat;
	private Long amountSat;
	private Double amountBtc;
	private Double amountEth;
	private Double amountStx;

	public RateModel() {
		super();
	}
	public static class Deserializer extends StdDeserializer<RateModel> {

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public RateModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			RateModel im = new RateModel();
			im.setFiatCurrency(node.get("fiatCurrency").asText());
			im.setSymbol(node.get("symbol").asText());
			im.setAmountSat(node.get("amountSat").asLong());
			im.setAmountFiat(node.get("amountFiat").asDouble());
			im.setAmountBtc(node.get("amountBtc").asDouble());
			im.setAmountStx(node.get("amountStx").asDouble());
			im.setAmountEth(node.get("amountEth").asDouble());
			return im;
		}
	}
}
