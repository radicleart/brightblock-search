package org.brightblock.search.api.model;

import java.io.Serializable;

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
public class TradeInfoModel implements Serializable {

	private static final long serialVersionUID = -7326697911835386794L;
	private Integer saleType;
	private Long buyNowOrStartingPrice;
	private Long incrementPrice;
	private Long reservePrice;
	private Long biddingEndTime;

	public TradeInfoModel() {
		super();
	}

	//@JsonCreator
	//public TradeInfoModel(@JsonProperty("saleType") Integer saleType, 
	//	super();
	//	this.saleType = saleType;
	//}

}
