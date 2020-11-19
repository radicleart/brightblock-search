package org.brightblock.search.api.model;

import java.io.Serializable;
import java.util.List;

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
public class ClarityApplicationModel implements Serializable {

	private static final long serialVersionUID = -4314545946262418883L;
	private Integer appCounter;
	private Integer status;
	private Integer storageModel;
	private Long mintCounter;
	private String owner;
	private String baseTokenUri;
	private String contractId;
	private List<ClarityAssetModel> clarityAssets;

	public ClarityApplicationModel() {
		super();
	}
}
