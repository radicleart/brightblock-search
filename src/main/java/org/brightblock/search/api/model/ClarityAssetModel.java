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
public class ClarityAssetModel implements Serializable {

	private static final long serialVersionUID = 5537467791208033623L;
	private String assetHash;
	private Long nftIndex;
	private String owner;
	private Long age;
	private TradeInfoModel tradeInfo;

	public ClarityAssetModel() {
		super();
	}
}
