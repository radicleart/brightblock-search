package org.brightblock.search.api.model;

import org.springframework.data.annotation.TypeAlias;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TypeAlias(value = "KeywordModel")
public class TradeInfoModel {

	private Long saleType;
	private Double buyNowOrStartingPrice;
	private Double incrementPrice;
	private Double reservePrice;
	private Long biddingEndTime;

//	public TradeInfoModel() {
//		super();
//	}
//
//	public static class Deserializer extends StdDeserializer<TradeInfoModel> {
//
//		private static final long serialVersionUID = 6940896872735534114L;
//
//		public Deserializer() {
//			this(null);
//		}
//
//		Deserializer(Class<?> vc) {
//			super(vc);
//		}
//
//		@Override
//		public TradeInfoModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
//			JsonNode node = jp.getCodec().readTree(jp);
//			TradeInfoModel im = new TradeInfoModel();
//			im.setSaleType(node.get("saleType").asLong());
//			im.setBuyNowOrStartingPrice(node.get("buyNowOrStartingPrice").asDouble());
//			im.setIncrementPrice(node.get("incrementPrice").asDouble());
//			im.setReservePrice(node.get("reservePrice").asDouble());
//			im.setBiddingEndTime(node.get("biddingEndTime").asLong());
//			return im;
//		}
//	}

}
