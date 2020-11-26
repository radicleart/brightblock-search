package org.brightblock.search.api.model;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@JsonDeserialize(using = TradeInfoModel.Deserializer.class)
public class TradeInfoModel implements Serializable {

	private static final long serialVersionUID = -7326697911835386794L;
	private Long saleType;
	private Double buyNowOrStartingPrice;
	private Double incrementPrice;
	private Double reservePrice;
	private Long biddingEndTime;

	public TradeInfoModel() {
		super();
	}

	public static class Deserializer extends StdDeserializer<TradeInfoModel> {

		private static final long serialVersionUID = 6940896872735534114L;

		public Deserializer() {
			this(null);
		}

		Deserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public TradeInfoModel deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
			JsonNode node = jp.getCodec().readTree(jp);
			TradeInfoModel im = new TradeInfoModel();
			im.setSaleType(node.get("saleType").asLong());
			im.setBuyNowOrStartingPrice(node.get("buyNowOrStartingPrice").asDouble());
			im.setIncrementPrice(node.get("incrementPrice").asDouble());
			im.setReservePrice(node.get("reservePrice").asDouble());
			im.setBiddingEndTime(node.get("biddingEndTime").asLong());
			return im;
		}
	}

	public Long getSaleType() {
		return saleType;
	}

	public void setSaleType(Long saleType) {
		this.saleType = saleType;
	}

	public Double getBuyNowOrStartingPrice() {
		return buyNowOrStartingPrice;
	}

	public void setBuyNowOrStartingPrice(Double buyNowOrStartingPrice) {
		this.buyNowOrStartingPrice = buyNowOrStartingPrice;
	}

	public Double getIncrementPrice() {
		return incrementPrice;
	}

	public void setIncrementPrice(Double incrementPrice) {
		this.incrementPrice = incrementPrice;
	}

	public Double getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(Double reservePrice) {
		this.reservePrice = reservePrice;
	}

	public Long getBiddingEndTime() {
		return biddingEndTime;
	}

	public void setBiddingEndTime(Long biddingEndTime) {
		this.biddingEndTime = biddingEndTime;
	}

}
